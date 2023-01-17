package com.carwash.telegram.service;

import com.carwash.telegram.commands.Commands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class BotMenuService {
    private List<BotCommand> listOfCommand = new ArrayList<>();

    public BotMenuService() {
        this.addBotCommand();
    }

    public void addBotCommand() {
        listOfCommand.add(new BotCommand(Commands.START, "начать сеанс"));
        listOfCommand.add(new BotCommand(Commands.LOGIN, "вход по логину и паролю (имени и номеру телефона)"));
        listOfCommand.add(new BotCommand(Commands.REGISTER, "регистрация"));
        listOfCommand.add(new BotCommand(Commands.ALL_CITY, "список городов"));
        listOfCommand.add(new BotCommand(Commands.ALL_CAR_WASH, "список моек в Вашем городе"));
        listOfCommand.add(new BotCommand(Commands.NEA_CAR_WASH, "список ближайших моек"));
        listOfCommand.add(new BotCommand(Commands.STATUS, "статус заказа"));
        listOfCommand.add(new BotCommand(Commands.HISTORY, "история всех Ваших заказов"));
        listOfCommand.add(new BotCommand(Commands.ORDER_ON, "запись на мойку"));
        listOfCommand.add(new BotCommand(Commands.ORDER_OFF, "обнуление заказа"));
        listOfCommand.add(new BotCommand(Commands.HELP, "справка"));
    }

    public List<BotCommand> getListOfCommand() {
        return listOfCommand;
    }
}
