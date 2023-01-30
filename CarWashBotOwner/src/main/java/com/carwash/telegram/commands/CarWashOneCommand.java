package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.CarWashForOwnerDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class CarWashOneCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashOneCommand(BotUserService botUserService,
                             BotController botController) {
        super("one", "описание одной мойки\n", botUserService, botController);
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

        botUser.setIdCarWash(Long.valueOf(callbackData));
        botUser = botUserService.update(botUser);

        HttpAnswer httpAnswer = botController.getCarWashById(botUser.getIdCarWash());

        if (!httpAnswer.isSuccess()) {
            answer.setText(httpAnswer.getStatus());
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.CAR_WASH_LIST);
            botUserService.save(botUser);

            return;
        }

        CarWashForOwnerDto carWashForOwnerDto = (CarWashForOwnerDto) httpAnswer.getObject();

        answer.setText(carWashForOwnerDto.toString());

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard();
        answer.setReplyMarkup(keyboardMarkup);

        execute(absSender, answer, user);

        botUser.setStepService(BotUserStepService.CAR_WASH_ONE);
        botUserService.save(botUser);
    }

    private InlineKeyboardMarkup getInlineKeyboard() {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;

        //-----------------------------------
        rowInLine = new ArrayList<>();
/*
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_DELETE);
        btn.setCallbackData(BotText.BTN_DELETE);
        rowInLine.add(btn);

 */

        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_UPDATE);
        btn.setCallbackData(BotText.BTN_UPDATE);
        rowInLine.add(btn);

        /*
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_WEEKEND);
        btn.setCallbackData(BotText.BTN_WEEKEND);
        rowInLine.add(btn);

         */

        rowsInLine.add(rowInLine);

        //-----------------------------------
        rowInLine = new ArrayList<>();

        /*
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_TIME_NOW);
        btn.setCallbackData(BotText.BTN_TIME_NOW);
        rowInLine.add(btn);

         */

        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_TIME_BY_DATE);
        btn.setCallbackData(BotText.BTN_TIME_BY_DATE);
        rowInLine.add(btn);

        rowsInLine.add(rowInLine);

        //-----------------------------------
        /*
        rowInLine = new ArrayList<>();

        btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_CANCEL_TIME_NOW);
        btn.setCallbackData(BotText.BTN_CANCEL_TIME_NOW);
        rowInLine.add(btn);

                btn =  new InlineKeyboardButton();
        btn.setText(BotText.BTN_CANCEL_TIME_BY_DATE);
        btn.setCallbackData(BotText.BTN_CANCEL_TIME_BY_DATE);
        rowInLine.add(btn);

        rowsInLine.add(rowInLine);

         */
        //-----------------------------------

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }
}