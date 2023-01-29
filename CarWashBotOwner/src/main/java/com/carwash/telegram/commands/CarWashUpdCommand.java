package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class CarWashUpdCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashUpdCommand(BotUserService botUserService,
                             BotController botController) {
        super("update", "для изменяемой автомойки ввести адрес\n", botUserService, botController);
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

        botUser.setPrevStepService(BotUserStepService.CAR_WASH_UPDATE);
        botUser.setStepService(BotUserStepService.CAR_WASH_LIST_ADD_ADDRESS);
        botUserService.save(botUser);

        answer.setText(BotText.ADD_INPUT_COORD);
        execute(absSender, answer, user);
    }


}