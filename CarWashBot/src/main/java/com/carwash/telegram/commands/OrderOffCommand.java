package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.TimeTableDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import com.carwash.telegram.util.UncheckedConversion;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class OrderOffCommand extends SimpleCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOffCommand(BotUserService botUserService,
                           BotController botController) {
        super("off", "Отменить заказ\n", botUserService, botController);
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

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(user));

        if(!botUser.isIslogin()) {
            log.info("User {} is trying to send message without starting the bot!", user.getId());
            answer.setText(BotText.LOGIN_OR_REGISTRY);
            execute(absSender, answer, user);
            return;
        }

        //botUserService.setServiceStep( botUser.getId(), BotUserStepService.ORDER_OFF);

        HttpAnswer httpAnswer = botController.getListOrderOff(botUser.getIdUser());

        if (!httpAnswer.isSuccess()) {
            answer.setText(httpAnswer.getStatus());
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);
            return;
        }

        List rawList = httpAnswer.getObjectList();
        List<TimeTableDto> allTimeTable = UncheckedConversion.castList(TimeTableDto.class, rawList);

        if (allTimeTable.size() == 0) {
            answer.setText(BotText.LIST_ACTIVE_ORDER_EMPTY);
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);
            return;
        }

        answer = getSelectCommandButton(chat, allTimeTable);
        execute(absSender, answer, user);

        botUser.setStepService(BotUserStepService.ORDER_OFF_SELECT_ORDER);
        botUserService.save(botUser);

    }

    public SendMessage getSelectCommandButton(Chat chat, List<TimeTableDto> allTimeTable) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));

        //message.setText(BotText.SELECT_TIME);
        message.setText(BotText.LIST_ACTIVE_ORDER);
        message.enableHtml(true);

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

        StringBuilder sb;

        for (TimeTableDto timeTableDto: allTimeTable) {

            rowInLine = new ArrayList<>();

            btn =  new InlineKeyboardButton();
            time = timeTableDto.getDateTable();

            sb = new StringBuilder();

            sb.append(time, 0, 16)
                    .append(" ")
                    .append(timeTableDto.getAddress());

            btn.setText(sb.toString());
            btn.setCallbackData(timeTableDto.getDateTable());
            rowInLine.add(btn);

            rowsInLine.add(rowInLine);
        }

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }
}