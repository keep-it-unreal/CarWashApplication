package com.carwash.telegram.core;

import com.carwash.telegram.commands.*;
import com.carwash.telegram.config.BotConfig;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;


@Slf4j
@Service
public class CommandBot extends TelegramLongPollingCommandBot {

    private final BotUserService botUserService;
    private final BotController botController;

    private final BotConfig botConfig;

    private HelpCommand helpCommand;

    public CommandBot(BotUserService botUserService, BotController botController, BotConfig botConfig) {
        this.botUserService = botUserService;
        this.botController = botController;
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


    private String getNameFromUpdate(Update update) {
        return update.getMessage().getChat().getFirstName() + " " + update.getMessage().getChat().getLastName();
    }


    @PostConstruct
    public void init() {

        log.info("Class = CommandBot. Initializing CommandBot...");

        // регистрация всех кастомных команд
        log.info("Class = CommandBot. Registering commands...");
        log.info("Class = CommandBot. Registering '/start'...");
        register(new StartCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/login'...");
        register(new LoginCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/register'...");
        register(new RegisterCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/order'...");
        register(new OrderOnCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/orders'...");
        register(new ListOrderOnCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/off'...");
        register(new OrderOffCommand(botUserService, botController));
        log.info("Class = CommandBot. History '/off'...");
        register(new HistoryCommand(botUserService, botController));

        log.info("Class = CommandBot. Registering '/stop'...");
        register(new StopCommand(botUserService, botController));

        helpCommand = new HelpCommand(this, botUserService, botController);
        log.info("Class = CommandBot. Registering '/help'...");
        register(helpCommand);

        // обработка неизвестной команды
        log.info("Class = CommandBot. Registering default action'...");
        registerDefaultAction(((absSender, message) -> {
            UnknownCommand(message);
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        }));
    }

    //Обрабатываем ответ на нажатие кнопки
    public void processCallbackQuery(Update update) {

        log.info("Processing CallbackQuery...");

        User user = update.getCallbackQuery().getFrom();
        log.info("MESSAGE_PROCESSING user = {}", user.getId());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(user));

        if (botUser.getStepService() == BotUserStepService.REGISTRY_SELECT_CITY) {

            RegisterSelectCityCommand registerSelectCityCommand = new RegisterSelectCityCommand(botUserService, botController);
            registerSelectCityCommand.execute(this, update, user, botUser);

        } else if (botUser.getStepService() == BotUserStepService.ORDER_ON_NEAR_OR_ALL) {

            OrderOnNearOrAll orderOnNearOrAll = new OrderOnNearOrAll (botUserService, botController);
            orderOnNearOrAll.execute(this, update, user, botUser);

        } else if (botUser.getStepService() == BotUserStepService.ORDER_ON_SELECT_CAR_WASH) {

            OrderOnSelectCarWashCommand orderOnSelectCarWashCommand = new OrderOnSelectCarWashCommand (botUserService, botController);
            orderOnSelectCarWashCommand.execute(this, update, user, botUser);

        } else if (botUser.getStepService() == BotUserStepService.ORDER_ON_SELECT_TIME) {

            OrderOnSelectTimeCommand orderOnSelectTimeCommand = new OrderOnSelectTimeCommand (botUserService, botController);
            orderOnSelectTimeCommand.execute(this, update, user, botUser);

        } else if (botUser.getStepService() == BotUserStepService.ORDER_OFF_SELECT_ORDER) {

            OrderOffSelectOrdreCommand orderOffSelectOrdreCommand = new OrderOffSelectOrdreCommand (botUserService, botController);
            orderOffSelectOrdreCommand.execute(this, update, user, botUser);

        } else {
            log.info("The user {} pressed the button at an unknown step {}", update.getCallbackQuery().getMessage().getFrom().getId(), botUser.getStepService());
            UnknownCommand(update.getMessage());
        }
    }

    // обработка сообщения не начинающегося с '/'
    @Override
    public void processNonCommandUpdate(Update update) {

        log.info("Processing non-command update...");

        // если обрабатываем ответ на нажатие кнопки
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
            return;
        }

        if (update.getMessage() == null || update.getMessage().getFrom() == null) {
            log.error("Update doesn't have a body!");
            return;
        }


        log.info("MESSAGE_PROCESSING user = {}", update.getMessage().getFrom().getId());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(update.getMessage().getFrom()));
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Chat chat = msg.getChat();
        String text = msg.getText();

        if (botUser.getStepService() == BotUserStepService.NEAR_CAR_WASH &&
                update.getMessage().getLocation() != null) {

            text = update.getMessage().getLocation().getLatitude() + " " + update.getMessage().getLocation().getLongitude();
            OrderOnNearCarWash orderOnNearCarWash = new OrderOnNearCarWash(botUserService, botController);
            orderOnNearCarWash.execute(this, user, chat, text, botUser);

            return;
        }

        if (!update.hasMessage()) {
            log.error("Update doesn't have a body!");
            return;
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getMessage().getChat().getId());

        if (!canSendMessage(user, msg, botUser)) {
            return;
        }

        if (botUser.getStepService() == BotUserStepService.LOGIN) {

            LoginInputPhone loginInputPhone = new LoginInputPhone(botUserService, botController);
            loginInputPhone.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.REGISTER) {

            RegisterInputPhone registerInputPhone = new RegisterInputPhone(botUserService, botController);
            registerInputPhone.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.ORDER_ON_INPUT_DATE) {

            OrderOnInputDate orderOnInputDate = new OrderOnInputDate(botUserService, botController);
            orderOnInputDate.execute(this, user, chat, text, botUser);

        } else
        if (botUser.getStepService() == BotUserStepService.NEAR_CAR_WASH) {

            OrderOnNearCarWash orderOnNearCarWash = new OrderOnNearCarWash(botUserService, botController);
            orderOnNearCarWash.execute(this, user, chat, text, botUser);

        } else {
            log.info("Warning!!! Selection completed, option not selected");
        }
    }


    private void UnknownCommand(Message message) {

        log.info("User {} is trying to execute unknown command '{}'.", message.getFrom().getId(), message.getText());

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(message.getText() + ". " + BotText.UNKNOWN_COMMAND);

        replyToUser(answer, message.getFrom());
    }


    /**
     * Предварительная проверка аргументов перед выполнением команды
     *
     * @param user данные о пользователе, отправившем команду
     * @param msg обрабатываемое сообщение
     * @param botUser данные, связанные с действиями пользователя
     */
    private boolean canSendMessage(User user, Message msg, BotUser botUser) {

        SendMessage answer = new SendMessage();
        answer.setChatId(msg.getChatId());

        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            log.info("User {} is trying to send empty message!", user.getId());
            answer.setText(BotText.EMPTY_MESSAGE);
            replyToUser(answer, user);
            return false;
        }

        if (botUser.getStepService() != BotUserStepService.LOGIN &&
                botUser.getStepService() != BotUserStepService.REGISTER &&
                !botUser.isIslogin()) {

            log.info("User {} is trying to send message without starting the bot!", user.getId());
            answer.setText(BotText.LOGIN_OR_REGISTRY);
            replyToUser(answer, user);
            return false;
        }

        return true;
    }

    private void replyToUser(SendMessage message, User user) {
        try {
            execute(message);
            log.info("UserId = {} send message = {}", user.getId(), message.getText());
        } catch (TelegramApiException e) {
            log.error("MESSAGE_EXCEPTION userId = {}, e = {}", user.getId(), e);
        }
    }

    private String getNameFromUser(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
