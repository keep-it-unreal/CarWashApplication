package com.carwash.telegram.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EditMessageService {
    public EditMessageText editTextMessage(Update update, String newText) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(newText);
        editMessageText.setMessageId((int) messageId);

        return editMessageText;
    }
}
