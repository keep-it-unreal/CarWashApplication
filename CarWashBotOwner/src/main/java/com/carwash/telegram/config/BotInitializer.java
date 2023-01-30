package com.carwash.telegram.config;

import com.carwash.telegram.core.CommandBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotInitializer {

    @Autowired
    private CommandBot commandBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(commandBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static class BotMenuService {
        private List<BotCommand> listOfCommand = new ArrayList<>();

        public BotMenuService() {
            this.addBotCommand();
        }

        public void addBotCommand() {
            listOfCommand.add(new BotCommand("/start", "начало работы с ботом\n"));
            listOfCommand.add(new BotCommand("/login", "вход по имени и номеру телефона\n"));
            listOfCommand.add(new BotCommand("/register", "регистрация в сервисе\n"));
            listOfCommand.add(new BotCommand("/list", "список ваших автомоек\n"));



            listOfCommand.add(new BotCommand("/stop", "завершение работы с ботом\n"));
            listOfCommand.add(new BotCommand("/help", "список всех команд\n"));
        }

        public List<BotCommand> getListOfCommand() {
            return listOfCommand;
        }
    }
}
