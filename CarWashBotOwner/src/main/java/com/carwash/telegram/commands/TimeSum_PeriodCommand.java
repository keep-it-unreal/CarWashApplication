package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
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
public final class TimeSum_PeriodCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public TimeSum_PeriodCommand(BotUserService botUserService,
                                 BotController botController) {
        super("time-sum-period", "Ввести даты периода через пробел в формате dd.mm.yyyy dd.mm.yyyy\n", botUserService, botController);
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

        String period = text;
        if (!ValidatePeriod(period)) {
            log.info("User {} entered invalid period dates", user.getId());
            answer.setText(BotText.TIME_SUM_PERIOD);
            execute(absSender, answer, user);
            return;
        }

        String[] parts = period.split(" ");
        botUser.setDateBegin(parts[0]);
        botUser.setDateEnd(parts[1]);
        botUser = botUserService.update(botUser);

        HttpAnswer httpAnswer = botController.getSumReportFromAllTimeTableByOwner(botUser.getIdUser(), botUser.getDateBegin(), botUser.getDateEnd());

        if (!httpAnswer.isSuccess()) {
            answer.setText(httpAnswer.getStatus());
            execute(absSender, answer, user);

            botUser.setStepService(BotUserStepService.CAR_WASH_LIST);
            botUserService.save(botUser);

            return;
        }

        List rawList = httpAnswer.getObjectList();
        List<TimeTableSumReportDto> timeTableSumReportDtoList = UncheckedConversion.castList(TimeTableSumReportDto.class, rawList);

        botUser.setStepService(BotUserStepService.TIME_SUM_LIST);
        botUserService.save(botUser);

        answer = getSelectCommandButton(chat, timeTableSumReportDtoList);
        execute(absSender, answer, user);
    }

    public SendMessage getSelectCommandButton(Chat chat, List<TimeTableSumReportDto> timeTableSumReportDtoList) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));

        message.setText(BotText.TIME_SUM_LIST);
        message.enableHtml(true);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(timeTableSumReportDtoList);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<TimeTableSumReportDto> timeTableSumReportDtoList) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;
        String st;

        for (TimeTableSumReportDto timeTableSumReportDto : timeTableSumReportDtoList) {

            rowInLine = new ArrayList<>();

            /*
            st = timeTableSumReportDto.getAddress() + ": " +
                    timeTableSumReportDto.getCh() + " клиентов, " +
                    timeTableSumReportDto.getPrice() * timeTableSumReportDto.getCh() + " руб.";
             */

            st = " - Адрес: " + timeTableSumReportDto.getAddress() + " - " +
                    "Кол-во клиентов: " + timeTableSumReportDto.getCh() + " - " +
                    "Итого: " + timeTableSumReportDto.getPrice() * timeTableSumReportDto.getCh() + " руб. - ";

            btn =  new InlineKeyboardButton();
            btn.setText(st);
            btn.setCallbackData(timeTableSumReportDto.getIdCarWash().toString());
            rowInLine.add(btn);

            rowsInLine.add(rowInLine);

        }

        keyboardMarkup.setKeyboard(rowsInLine);

        return keyboardMarkup;
    }

    /**
     * Проверка введеных дат периода на соответствие шаблону
     * @param text - текст сообщения, должен содержать только даты периода в формате dd.mm.yyyy dd.mm.yyyy
     * @return - true, если проверка успешна
     */
    private boolean ValidatePeriod(String text) {

        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {

            String[] parts = text.split(" ");
            try {
                Instant iBegin = DateConverter.StringToInstant(parts[0]);
                Instant iEnd = DateConverter.StringToInstant(parts[1]);

                //if (Instant.now().isAfter(iBegin) & Instant.now().isAfter(iEnd) & iEnd.isAfter(iBegin) ) {
                if (iEnd.isAfter(iBegin) ) {
                    return true;
                }

            } catch (Exception ex) {
                log.info("Exception = {}", ex);
            }
        }

        return false;
    }

}