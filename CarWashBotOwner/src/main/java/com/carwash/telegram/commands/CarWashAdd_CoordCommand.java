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
public final class CarWashAdd_CoordCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashAdd_CoordCommand(BotUserService botUserService,
                                   BotController botController) {
        super("add-coord", "для добавляемой автомойки ввести координаты\n", botUserService, botController);
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

        String coord = text;
        if (!ValidateCoord(coord)) {
            log.info("The user {} specified an incorrect coordinate format.", user.getId());
            message.setText(BotText.ADD_INPUT_COORD);
            execute(absSender, message, user);
            return;
        }

        String[] parts = text.split(" ");
        botUser.setLatitude(Double.parseDouble(parts[0]));
        botUser.setLongitude(Double.parseDouble(parts[1]));
        botUser = botUserService.update(botUser);

        botUserService.setServiceStep( botUser.getId(), BotUserStepService.CAR_WASH_LIST_ADD_COORD);

        message.setText(BotText.ADD_INPUT_BEGIN_END);
        execute(absSender, message, user);
    }

    /**
     * Проверка введеных координат на соответствие шаблону
     * @param text - текст сообщения, должен содержать только координаты в формате хх.ххххх xx.xxxxx
     * @return - true, если проверка успешна
     */
    private boolean ValidateCoord(String text) {

        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{2}\\.\\d{2,6}\\s\\d{2}\\.\\d{2,6}$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        }

        return false;
    }

}