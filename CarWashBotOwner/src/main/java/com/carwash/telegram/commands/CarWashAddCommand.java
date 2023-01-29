package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class CarWashAddCommand extends SimpleCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashAddCommand(BotUserService botUserService,
                             BotController botController) {
        super("add", "Добавить мойку\n", botUserService, botController);
    }

    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     *
     * @param absSender - отправляет ответ пользователю
     * @param user      - пользователь, который выполнил команду
     * @param chat      - чат бота и пользователя
     * @param strings   - аргументы, переданные с командой
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info("COMMAND_PROCESSING userId = {} commandId = {}", user.getId(), getCommandIdentifier());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(user));

        if (!botUser.isIslogin()) {
            log.info("User {} is trying to send message without starting the bot!", user.getId());
            answer.setText(BotText.LOGIN_OR_REGISTRY);
            execute(absSender, answer, user);
            return;
        }

        botUserService.setServiceStep(botUser.getId(), BotUserStepService.CAR_WASH_LIST_ADD);

        answer.setText(BotText.ADD_INPUT_ADDRESS);
        execute(absSender, answer, user);
    }

    /**
     * обработка сообщения, введенного пользователем
     *
     * @param absSender - отправляет ответ пользователю
     * @param update    - сообщение
     * @param user      - пользователь, который выполнил команду
     * @param botUser   - данные, связанные с действиями пользователя
     */

    public void execute(AbsSender absSender, Update update, User user, BotUser botUser) {

        String callbackData = update.getCallbackQuery().getData();

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getCallbackQuery().getMessage().getChat().getId());

        botUserService.setServiceStep(botUser.getId(), BotUserStepService.CAR_WASH_LIST_ADD);

        answer.setText(BotText.ADD_INPUT_ADDRESS);
        execute(absSender, answer, user);
    }
}
