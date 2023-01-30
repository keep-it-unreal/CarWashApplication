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
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class CarWashAdd_hhBeginEndCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashAdd_hhBeginEndCommand(BotUserService botUserService,
                                        BotController botController) {
        super("add-hour", "для добавляемой автомойки ввести время начала-окончания работы( в часах)\n", botUserService, botController);
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

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String hh = text;
        if (!ValidateHh(hh)) {
            log.info("The user {} specified an incorrect coordinate format.", user.getId());
            message.setText(BotText.ADD_INPUT_BEGIN_END);
            execute(absSender, message, user);
            return;
        }

        String[] parts = text.split(" ");
        botUser.setDailyStartTime(parts[0]);
        botUser.setDailyEndTime(parts[1]);
        botUser = botUserService.update(botUser);

        botUserService.setServiceStep( botUser.getId(), BotUserStepService.CAR_WASH_LIST_ADD_BEGIN_END);

        message.setText(BotText.ADD_INPUT_PRICE);
        execute(absSender, message, user);
    }

    /**
     * Проверка введеных hh начала - окончания работы на соответствие шаблону
     * @param text - текст сообщения, должен содержать только время начала - окончания работы в формате хх xx
     * @return - true, если проверка успешна
     */
    private boolean ValidateHh(String text) {

        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{1,2}\\s\\d{1,2}$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {

            String[] parts = text.split(" ");
            String Begin = parts[0];
            String End = parts[1];

            try {
                Integer hBegin = Integer.valueOf(Begin);
                Integer hEnd = Integer.valueOf(End);
                if (hBegin < 0 || hBegin > 23 || hEnd < 1 || hEnd > 24 || hBegin > hEnd) {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
            return true;
        }
        return false;
    }

}