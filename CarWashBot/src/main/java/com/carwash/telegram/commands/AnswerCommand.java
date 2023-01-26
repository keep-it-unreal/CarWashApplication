package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
abstract class AnswerCommand extends BotCommand {

    final BotUserService botUserService;
    final BotController botController;

    String commandIdentifier;

    AnswerCommand(String commandIdentifier,
                  String description,
                  BotUserService botUserService,
                  BotController botController) {
        super(commandIdentifier, description);
        this.commandIdentifier=commandIdentifier;
        this.botUserService = botUserService;
        this.botController = botController;
    }

    void execute(AbsSender sender, SendMessage message, User user) {
        try {
            sender.execute(message);
            log.info("for userId = {} commandId = {} COMMAND_SUCCESS", user.getId(), getCommandIdentifier());

        } catch (TelegramApiException e) {
            log.info("for userId = {} commandId = {} COMMAND_EXCEPTION = {}", user.getId(), getCommandIdentifier(), e);
        }
    }

    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        /*
        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        message.setText(sb.toString());
        execute(absSender, message, user);

         */
    }

    protected String getNameFromUser(User user) {
        return user.getFirstName() + " "+ user.getLastName();
    }


}