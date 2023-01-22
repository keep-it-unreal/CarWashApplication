package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class OrderOnInputDate extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOnInputDate(BotUserService botUserService,
                            BotController botController) {
        super("order_on_input_date", "Ввести дату для записи на мойку\n", botUserService, botController);
    }

    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param text - текст сообщения
     * @param botUser - данные, связанные с действиями пользователя
     */
    public void execute(AbsSender absSender, User user, Chat chat, String text, BotUser botUser) {

        log.info("MESSAGE_PROCESSING userId = {} message = {}", user.getId(), text);

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String date;
        if (validateDate(text)) {
            date = text;

        } else {
            log.info("User {}. Invalid date format.", user.getId());
            message.setText(BotText.INPUT_DATE);
            execute(absSender, message, user);
            return;
        }

        botUser.setDateOrder(date);
        botUser.setStepService(BotUserStepService.ORDER_ON_NEAR_OR_ALL);
        botUserService.save(botUser);

        message.setText(BotText.LIST_NEAR_OR_ALL_CAR_WASH);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard();
        message.setReplyMarkup(keyboardMarkup);

        execute(absSender, message, user);
    }

    private InlineKeyboardMarkup getInlineKeyboard() {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.SELECT_ALL);
        btn.setCallbackData(BotText.SELECT_ALL);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.SELECT_NEAR);
        btn.setCallbackData(BotText.SELECT_NEAR);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

    private boolean validateDate(String stringDate) {

        if (stringDate == null) {
            return false;
        }

        Instant instant=null;

        try {
            String europeanDatePattern = "dd.MM.yyyy";
            DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);
            LocalDate localDate = LocalDate.parse(stringDate,europeanDateFormatter);
            ZoneId zoneId = ZoneId.systemDefault();
            instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            int compareResult = Instant.now().compareTo(instant);

            if (compareResult > 0) {
                return false;
            }

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}