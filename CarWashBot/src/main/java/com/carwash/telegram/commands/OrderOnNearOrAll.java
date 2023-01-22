package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.dto.CarWashDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class OrderOnNearOrAll extends AnswerCommand {

    private List<CarWashDto> allCarWash;

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOnNearOrAll(BotUserService botUserService,
                            BotController botController) {
        super("order_on_new_or_all", "Выбор - для оформления заказа вывести список всех моек или ближайших\n", botUserService, botController);
    }

    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param update - сообщение
     * @param user - пользователь, который выполнил команду
     * @param botUser - данные, связанные с действиями пользователя
     */
    public void execute(AbsSender absSender, Update update, User user, BotUser botUser) {

        String callbackData = update.getCallbackQuery().getData();

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getCallbackQuery().getMessage().getChat().getId());

        // вывести список всех авто-моек в городе user
        if (callbackData.equals(BotText.SELECT_ALL)) {
            allCarWash = botController.getAllCarWash(botUser.getId(), botUser.getDateOrder());
            answer = getSelectCommandButton(update.getCallbackQuery().getMessage().getChat(), allCarWash);
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.ORDER_ON_SELECT_CAR_WASH);
            botUserService.save(botUser);

        } else if (callbackData.equals(BotText.SELECT_NEAR)) {
            // запросить широту и долготу
            //carWashDtoList = botController.getNewCarWash();

        } else {

            answer.setText(BotText.UNKNOWN_ERROR);
            execute(absSender, answer, user);
            return;

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