package com.carwash.telegram.service;

import com.carwash.telegram.model.CarWashModel;
import com.carwash.telegram.model.CityModel;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SendMessageService {

    public SendMessage createTextMessage(Update update, String message) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createCallbackQuaryTextMessage(String message, Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createTextMessageListCarWash(Update update, List<? extends CarWashModel> message) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        StringBuilder stringBuilder = new StringBuilder();
        for (CarWashModel model : message) {
            stringBuilder.append(model).append("\n");
        }
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }

    public SendMessage createTextMessageListCity(Update update, List<? extends CityModel> message) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        StringBuilder stringBuilder = new StringBuilder();
        for (CityModel model : message) {
            stringBuilder.append(model).append("\n");
        }
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }

}
