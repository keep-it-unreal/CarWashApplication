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

@Slf4j
public final class CarWashAdd_AddressCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashAdd_AddressCommand(BotUserService botUserService,
                                     BotController botController) {
        super("add-address", "для добавляемой автомойки ввести адрес\n", botUserService, botController);
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

        botUser.setAddress(text);
        botUser = botUserService.update(botUser);

        botUserService.setServiceStep( botUser.getId(), BotUserStepService.CAR_WASH_LIST_ADD_ADDRESS);

        message.setText(BotText.ADD_INPUT_COORD);
        execute(absSender, message, user);
    }


}