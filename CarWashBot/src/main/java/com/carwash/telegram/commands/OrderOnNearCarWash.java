package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.CarWashDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import com.carwash.telegram.util.UncheckedConversion;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class OrderOnNearCarWash extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOnNearCarWash(BotUserService botUserService,
                              BotController botController) {
        super("order_on_near_car_wash", "ввод координат местонахождения\n", botUserService, botController);
    }

    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param text - текст сообщения
     * @param botUser - данные, связанные с действиями пользователя
     */
    public void execute(AbsSender absSender, User user, Chat chat, String text, BotUser botUser) {

        log.info("COMMAND_PROCESSING userId = {} commandId = {}", user.getId(), getCommandIdentifier());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        try {
            String[] parts = text.split(" ");
            String lattitude = parts[0];
            String longitude = parts[1];

            botUser.setLatitude(Double.parseDouble(lattitude));
            botUser.setLongitude(Double.parseDouble(longitude));
            botUser = botUserService.update(botUser);

            HttpAnswer httpAnswer = botController.getNewCarWash(botUser.getIdUser(), botUser.getDateOrder(), lattitude, longitude);

            if (!httpAnswer.isSuccess()) {
                answer.setText(httpAnswer.getStatus());
                execute(absSender, answer, user);

                botUser.setStepService(BotUserStepService.NONE);
                botUserService.save(botUser);

                return;
            }

            List rawList = httpAnswer.getObjectList();
            List<CarWashDto> allCarWash = UncheckedConversion.castList(CarWashDto.class, rawList);

            if (allCarWash.size() == 0) {
                answer.setText(BotText.LIST_CAR_WASH_EMPTY);
                execute(absSender, answer, user);

                botUser.setStepService(BotUserStepService.NONE);
                botUserService.save(botUser);
                return;
            }

            answer = getSelectCommandButton(chat, allCarWash);
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.ORDER_ON_SELECT_CAR_WASH);
            botUserService.save(botUser);


        } catch (Exception ex) {
            log.info("User {} entered invalid coordinates", user.getId());
            answer.setText(BotText.ORDER_COORD_TEXT);
            execute(absSender, answer, user);
        }
    }

    public SendMessage getSelectCommandButton(Chat chat, List<CarWashDto> all) {

        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.SELECT_CAR_WASH);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(all);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<CarWashDto> all) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;

        for (CarWashDto carWashDto: all) {
            rowInLine = new ArrayList<>();
            btn =  new InlineKeyboardButton();
            btn.setText("< Адрес: " + carWashDto.getAddress() + " ( " + carWashDto.getPrice() + " руб.) >");
            btn.setCallbackData(String.valueOf(carWashDto.getId()));
            rowInLine.add(btn);
            rowsInLine.add(rowInLine);
        }

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.CANCEL);
        btn.setCallbackData(BotText.CANCEL);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }
}