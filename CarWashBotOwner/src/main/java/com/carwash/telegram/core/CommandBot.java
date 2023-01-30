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

        log.info("Class = CommandBot. Registering '/list'...");
        register(new CarWashListCommand(botUserService, botController));
        log.info("Class = CommandBot. Registering '/add'...");
        register(new CarWashAddCommand(botUserService, botController));



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

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_LIST) {

            String callbackData = update.getCallbackQuery().getData();

            // <ins> CarWash
            if (callbackData.equals(BotText.BTN_NSERT)) {
                CarWashAddCommand carWashAddCommand = new CarWashAddCommand(botUserService, botController);
                carWashAddCommand.execute(this, update, user, botUser);

                // <+> TimeTable for current month
            } else if (callbackData.equals(BotText.BTN_ADD_TIME_CUR)) {
                TimeAddCurCommand timeAddCurCommand = new TimeAddCurCommand(botUserService, botController);
                timeAddCurCommand.execute(this, update, user, botUser);

                // <+> TimeTable for next month
            } else if (callbackData.equals(BotText.BTN_ADD_TIME_NEXT)) {
                TimeAddNextCommand timeAddNextCommand = new TimeAddNextCommand(botUserService, botController);
                timeAddNextCommand.execute(this, update, user, botUser);

                // Report by Period
            } else if (callbackData.equals(BotText.BTN_LIST_TIME_LAST)) {
                TimeSumCommand timeSumCommand = new TimeSumCommand(botUserService, botController);
                timeSumCommand.execute(this, update, user, botUser);

                // CarWash byID
            } else {
                CarWashOneCommand carWashOneCommand = new CarWashOneCommand(botUserService, botController);
                carWashOneCommand.execute(this, update, user, botUser);

            }

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_ONE) {

            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.equals(BotText.BTN_DELETE)) {
                CarWashDelCommand carWashDelCommand = new CarWashDelCommand(botUserService, botController);
                carWashDelCommand.execute(this, update, user, botUser);

            } else if (callbackData.equals(BotText.BTN_UPDATE)) {
                CarWashUpdCommand carWashUpdCommand = new CarWashUpdCommand(botUserService, botController);
                carWashUpdCommand.execute(this, update, user, botUser);

            } else if (callbackData.equals(BotText.BTN_TIME_BY_DATE)) {
                TimeOrderCommand timeOrderCommand = new TimeOrderCommand(botUserService, botController);
                timeOrderCommand.execute(this, update, user, botUser);
            }

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

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_LIST_ADD) {

            CarWashAdd_AddressCommand carWashAdd_addressCommand = new CarWashAdd_AddressCommand(botUserService, botController);
            carWashAdd_addressCommand.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_LIST_ADD_ADDRESS) {

            CarWashAdd_CoordCommand carWashAdd_coordCommand = new CarWashAdd_CoordCommand(botUserService, botController);
            carWashAdd_coordCommand.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_LIST_ADD_COORD) {

            CarWashAdd_hhBeginEndCommand carWashAdd_hhBeginEndCommand = new CarWashAdd_hhBeginEndCommand(botUserService, botController);
            carWashAdd_hhBeginEndCommand.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.CAR_WASH_LIST_ADD_BEGIN_END) {

            CarWashAdd_PriceCommand carWashAdd_priceCommand = new CarWashAdd_PriceCommand(botUserService, botController);
            carWashAdd_priceCommand.execute(this, user, chat, text, botUser);

        } else if (botUser.getStepService() == BotUserStepService.TIME_SUM) {

            TimeSum_PeriodCommand timeSumPeriodCommand = new TimeSum_PeriodCommand(botUserService, botController);
            timeSumPeriodCommand.execute(this, user, chat, text, botUser);


        } else if (botUser.getStepService() == BotUserStepService.TIME_LIST_ORDER) {

            TimeOrder_DateCommand timeOrderDateCommand = new TimeOrder_DateCommand(botUserService, botController);
            timeOrderDateCommand.execute(this, user, chat, text, botUser);
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
