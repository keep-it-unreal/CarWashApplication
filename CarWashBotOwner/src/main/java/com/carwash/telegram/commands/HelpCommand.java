package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;

@Slf4j
public class HelpCommand extends SimpleCommand {

    private final ICommandRegistry mCommandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry,
                       BotUserService botUserService,
                       BotController botController) {
        super("help", "список всех команд\n", botUserService, botController);
        mCommandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info("COMMAND_PROCESSING userId = {} commandId = {}", user.getId(), getCommandIdentifier());

        StringBuilder helpMessageBuilder = new StringBuilder("<b>Доступные команды:</b>\n\n");

        mCommandRegistry.getRegisteredCommands().forEach(cmd -> helpMessageBuilder.append(cmd.toString()).append("\n"));

        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(helpMessageBuilder.toString());

        execute(absSender, helpMessage, user);
    }
}
