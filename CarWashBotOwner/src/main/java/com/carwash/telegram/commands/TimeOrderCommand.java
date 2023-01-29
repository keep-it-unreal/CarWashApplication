package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class TimeOrderCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public TimeOrderCommand(BotUserService botUserService,
                            BotController botController) {
        super("time-list-order", "отчет по заказам на выбранную мойку на выбранную дату\n", botUserService, botController);
    }


    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param update - сообщение
     * @param user - пользователь, который выполнил команду
     * @param botUser - данные, связанные с действиями пользователя
     */
    public void execute(AbsSender absSender, Update update, User user, BotUser botUser) {

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getCallbackQuery().getMessage().getChat().getId());

        botUserService.setServiceStep( botUser.getId(), BotUserStepService.TIME_LIST_ORDER);

        answer.setText(BotText.DATE_ORDER);
        execute(absSender, answer, user);
    }


}