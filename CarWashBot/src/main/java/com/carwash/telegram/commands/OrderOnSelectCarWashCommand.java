package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.dto.TimeTableDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class OrderOnSelectCarWashCommand extends AnswerCommand {

    private List<TimeTableDto> allTimeTable;

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOnSelectCarWashCommand(BotUserService botUserService,
                                       BotController botController) {
        super("order_on_select_car_wash", "Выбор автомойки для заказа\n", botUserService, botController);
    }

    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param update - сообщение
     * @param user - пользователь, который выполнил команду
     * @param botUser - данные, связанные с действиями пользователя
     */

    public void execute(AbsSender absSender, Update update, User user, BotUser botUser) {

        String callbackData = update.getCallbackQuery().getData();

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getCallbackQuery().getMessage().getChat().getId());

        if (callbackData.equals(BotText.CANCEL)) {

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);

            answer.setText(BotText.CANCEL_USER);

            execute(absSender, answer, user);

        } else {
            try {

                botUser.setIdCarWash(Long.valueOf(callbackData));
                botUser = botUserService.update(botUser);
                botUser.setStepService(BotUserStepService.ORDER_ON_SELECT_TIME);
                botUserService.save(botUser);

                allTimeTable = botController.getAllTime(botUser.getDateOrder(), botUser.getIdCarWash());
                answer = getSelectCommandButton(update.getCallbackQuery().getMessage().getChat(), allTimeTable);
                execute(absSender, answer, user);

            } catch (Exception ex) {
                answer.setText(BotText.UNKNOWN_ERROR);
                execute(absSender, answer, user);
            }

        }
    }

    public SendMessage getSelectCommandButton(Chat chat, List<TimeTableDto> allTimeTable) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.SELECT_TIME);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(allTimeTable);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<TimeTableDto> allTimeTable) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;
        String time;

        rowInLine = new ArrayList<>();
        for (int i=0; i < allTimeTable.size(); i++) {

            btn = new InlineKeyboardButton();
            time = allTimeTable.get(i).getDateTable();
            btn.setText(time.substring(11, 16));
            btn.setCallbackData(allTimeTable.get(i).getDateTable());
            rowInLine.add(btn);

            if (((i+1) % 5 == 0)) {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
            }
        }
        rowsInLine.add(rowInLine);

        /*
        for (TimeTableDto timeTableDto: allTimeTable) {

            rowInLine = new ArrayList<>();

            btn =  new InlineKeyboardButton();
            time = timeTableDto.getDateTable();
            btn.setText(time.substring(11,16));
            btn.setCallbackData(timeTableDto.getDateTable());
            rowInLine.add(btn);

            rowsInLine.add(rowInLine);
        }
        */

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.CANCEL);
        btn.setCallbackData(BotText.CANCEL);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

}