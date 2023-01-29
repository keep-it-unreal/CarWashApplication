package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.CarWashForOwnerDto;
import com.carwash.telegram.entity.dto.TimeTableDto;
import com.carwash.telegram.entity.dto.TimeTableSumReportDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import com.carwash.telegram.util.DateConverter;
import com.carwash.telegram.util.UncheckedConversion;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class TimeOrder_DateCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public TimeOrder_DateCommand(BotUserService botUserService,
                                 BotController botController) {
        super("time-list-order-date", "Ввести дату для отчета по заказам на выбранную мойку на выбранную дату\n", botUserService, botController);
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

        log.info("COMMAND_PROCESSING userId = {} commandId = {}", user.getId(), getCommandIdentifier());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        String date = text;
        if (!ValidateDate(date)) {
            log.info("User {} entered invalid data", user.getId());
            answer.setText(BotText.DATE_ORDER);
            execute(absSender, answer, user);
            return;
        }

        botUser.setDateOrder(text);
        botUser = botUserService.update(botUser);

        HttpAnswer httpAnswer = botController.getTimeTableByIdCarWashByDate(botUser.getIdCarWash(), botUser.getDateOrder());

        if (!httpAnswer.isSuccess()) {
            answer.setText(BotText.EMPTY);
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.CAR_WASH_ONE);
            botUserService.save(botUser);

            return;
        }

        List rawList = httpAnswer.getObjectList();
        List<TimeTableDto> timeTableSumReportDtoList = UncheckedConversion.castList(TimeTableDto.class, rawList);

        botUser.setStepService(BotUserStepService.TIME_LIST_ORDER);
        botUserService.save(botUser);

        answer = getSelectCommandButton(chat, timeTableSumReportDtoList);
        execute(absSender, answer, user);
    }

    public SendMessage getSelectCommandButton(Chat chat, List<TimeTableDto> timeTableSumReportDtoList) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));

        message.setText(BotText.TIME_TABLE_LISTt);
        message.enableHtml(true);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(timeTableSumReportDtoList);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<TimeTableDto> timeTableSumReportDtoList) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;
        String time;

        rowInLine = new ArrayList<>();
        for (int i=0; i < timeTableSumReportDtoList.size(); i++) {

            btn = new InlineKeyboardButton();
            time = timeTableSumReportDtoList.get(i).getDateTable();
            btn.setText(time.substring(11, 16));
            btn.setCallbackData(timeTableSumReportDtoList.get(i).getDateTable());
            rowInLine.add(btn);

            if (((i+1) % 5 == 0)) {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
            }
        }
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        return keyboardMarkup;
    }

    /**
     * Проверка введеной даты на соответствие шаблону
     * @param text - текст сообщения, должен содержать только дату в формате dd.mm.yyyy
     * @return - true, если проверка успешна
     */
    private boolean ValidateDate(String text) {

        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {

            try {
                Instant iDate = DateConverter.StringToInstant(text);
                return true;

            } catch (Exception ex) {
                log.info("Exception = {}", ex);
            }
        }

        return false;
    }


}