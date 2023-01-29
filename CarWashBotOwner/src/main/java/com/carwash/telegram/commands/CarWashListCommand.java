package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.CarWashForOwnerDto;
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
public final class CarWashListCommand extends SimpleCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashListCommand(BotUserService botUserService,
                              BotController botController) {
        super("list", "Получить список своих моек\n", botUserService, botController);
    }

    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param strings - аргументы, переданные с командой
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info("COMMAND_PROCESSING userId = {} commandId = {}", user.getId(), getCommandIdentifier());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(user));

        if(!botUser.isIslogin()) {
            log.info("User {} is trying to send message without starting the bot!", user.getId());
            answer.setText(BotText.LOGIN_OR_REGISTRY);
            execute(absSender, answer, user);
            return;
        }

        HttpAnswer httpAnswer = botController.getCarWashListForOwner(botUser.getIdUser());

        if (!httpAnswer.isSuccess()) {
            answer.setText(httpAnswer.getStatus());
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);
            return;
        }

        List rawList = httpAnswer.getObjectList();
        List<CarWashForOwnerDto> allCarWash = UncheckedConversion.castList(CarWashForOwnerDto.class, rawList);

        botUser.setStepService(BotUserStepService.CAR_WASH_LIST);
        botUserService.save(botUser);

        answer = getSelectCommandButton(chat, allCarWash);
        execute(absSender, answer, user);

    }

    public SendMessage getSelectCommandButton(Chat chat, List<CarWashForOwnerDto> allCarWash) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));

        message.setText(BotText.LIST_CARWASH);
        message.enableHtml(true);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(allCarWash);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<CarWashForOwnerDto> allCarWash) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;
        String address;
        Long id;
        String key="";

        for (CarWashForOwnerDto carWashForOwnerDto : allCarWash) {

            rowInLine = new ArrayList<>();

            id = carWashForOwnerDto.getId();
            address = carWashForOwnerDto.getAddress();

            /*
            btn =  new InlineKeyboardButton();
            key = BotText.BTN_EDIT + " " + id.toString();
            btn.setText(BotText.BTN_EDIT);
            btn.setCallbackData(key);
            rowInLine.add(btn);

            btn =  new InlineKeyboardButton();
            key = BotText.BTN_DELETE + " " + id.toString();
            btn.setText(BotText.BTN_DELETE);
            btn.setCallbackData(key);
            rowInLine.add(btn);
            */

            btn =  new InlineKeyboardButton();
            btn.setText(address);
            btn.setCallbackData(id.toString());
            rowInLine.add(btn);

            rowsInLine.add(rowInLine);
        }

        rowInLine = new ArrayList<>();

        btn =  new InlineKeyboardButton();
        key = BotText.BTN_NSERT;
        btn.setText(BotText.BTN_NSERT);
        btn.setCallbackData(key);
        rowInLine.add(btn);

        btn =  new InlineKeyboardButton();
        key = BotText.BTN_ADD_TIME_CUR;
        btn.setText(BotText.BTN_ADD_TIME_CUR);
        btn.setCallbackData(key);
        rowInLine.add(btn);

        btn =  new InlineKeyboardButton();
        key = BotText.BTN_ADD_TIME_NEXT;
        btn.setText(BotText.BTN_ADD_TIME_NEXT);
        btn.setCallbackData(key);
        rowInLine.add(btn);


        btn =  new InlineKeyboardButton();
        key = BotText.BTN_LIST_TIME_LAST;
        btn.setText(BotText.BTN_LIST_TIME_LAST);
        btn.setCallbackData(key);
        rowInLine.add(btn);

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        return keyboardMarkup;
    }
}