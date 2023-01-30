package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class StartCommand extends SimpleCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public StartCommand(BotUserService botUserService,
                        BotController botController) {
        super("start", "начало работы с ботом\n", botUserService, botController);
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

        botUserService.setServiceStep(getNameFromUser(user),BotUserStepService.NONE);

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        /*
        sb.append("Если Вы уже зарегистрированы, для входа введите команду:\n")
                .append("'/login <X-XXX-XXX-XX-XX>'\nwhere &lt;X-XXX-XXX-XX-XX&gt; это № телефона, если Вы уже регистрировались в сервисе.")
                .append("Для регистрации, введите команду:\n")
                .append("/register <X-XXX-XXX-XX-XX>'\n");
        */
        sb.append("Если Вы уже зарегистрированы в сервисе, \n")
                .append("для входа введите команду /login\n")
                .append("Для регистрации, введите команду /register\n");

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}