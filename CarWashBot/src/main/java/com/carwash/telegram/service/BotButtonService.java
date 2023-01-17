package com.carwash.telegram.service;

import com.carwash.telegram.commands.BotText;
import com.carwash.telegram.model.CityModel;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotButtonService {

    public SendMessage getSelectCityCommandButton(Update update, List<CityModel> allCity) {
        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.SELECT_CITY);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard_City(allCity);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard_City(List<CityModel> allCity) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;

        for (CityModel city: allCity) {
            rowInLine = new ArrayList<>();
            btn =  new InlineKeyboardButton();
            btn.setText(city.getName());
            btn.setCallbackData(city.getId().toString());
            rowInLine.add(btn);
            rowsInLine.add(rowInLine);
        }

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.ANOTHER_CITY);
        btn.setCallbackData(BotText.ANOTHER_CITY);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

    public SendMessage getSignUpCommandButton(Update update) {
        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.SIGN_UP_TEXT);
        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboardMarkup();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public SendMessage getOrderCommandButton(Update update) {
        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.ORDER_WONT_TEXT);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboardMarkup();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        var noButton = new InlineKeyboardButton();

        yesButton.setText("Да");
        yesButton.setCallbackData(BotText.YES_BUTTON);

        noButton.setText("Нет");
        noButton.setCallbackData(BotText.NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }
}
