package com.carwash.telegram.core;

import com.carwash.telegram.commands.*;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import javax.annotation.PostConstruct;


@Slf4j
@Service
@PropertySource("application.properties")
public class CommandBot extends TelegramLongPollingCommandBot {

    private final BotUserService botUserService;
    private final BotController botController;

    private HelpCommand helpCommand;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

    public CommandBot(BotUserService botUserService, BotController botController) {
        this.botUserService = botUserService;
        this.botController = botController;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }



    private String getNameFromUpdate(Update update) {
        String name = new StringBuilder()
                .append(update.getMessage().getChat().getFirstName())
                .append(" ")
                .append(update.getMessage().getChat().getLastName())
                .toString();
        return name;
    }


    @PostConstruct
    public void init() {

        log.info("Initializing CommandBot...");

        // регистрация всех кастомных команд
        log.info("Registering commands...");
        log.info("Registering '/start'...");
        register(new StartCommand(botUserService, botController));
        log.info("Registering '/login'...");
        register(new LoginCommand(botUserService, botController));
        log.info("Registering '/register'...");
        register(new RegisterCommand(botUserService, botController));
        log.info("Registering '/orderon'...");
        register(new OrderOnCommand(botUserService, botController));

        log.info("Registering '/stop'...");
        register(new StopCommand(botUserService, botController));

        helpCommand = new HelpCommand(this, botUserService, botController);
        log.info("Registering '/help'...");
        register(helpCommand);

        // обработка неизвестной команды
        log.info("Registering default action'...");
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

        if (!update.hasMessage()) {
            log.error("Update doesn't have a body!");
            return;
        }

        log.info("MESSAGE_PROCESSING user = {}", update.getMessage().getFrom().getId());

        BotUser botUser = botUserService.getBotUser(getNameFromUser(update.getMessage().getFrom()));

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getMessage().getChat().getId());

        Message msg = update.getMessage();
        User user = msg.getFrom();
        Chat chat = msg.getChat();
        String text = msg.getText();

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
            /*
            String text = update.getMessage().getText();
            String[] parts = text.split(" ");
            String lattid = parts[0];
            String longt = parts[1];

            neaCarWash = botController.getNeaCarWash(lattid, longt);
            executeMessage(sendMessageService.createTextMessage(update, neaCarWash));
            */
            onTest(msg);
        } else {

        }

        /*
        String clearMessage = msg.getText();
        String messageForUsers = String.format("%s:\n%s", mAnonymouses.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();

        // отправка ответа отправителю о том, что его сообщение получено
        answer.setText(clearMessage);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user);

        // отправка сообщения всем остальным пользователям бота
        answer.setText(messageForUsers);
        Stream<Anonymous> anonymouses = mAnonymouses.anonymouses();
        anonymouses.filter(a -> !a.getUser().equals(user))
                .forEach(a -> {
                    answer.setChatId(a.getChat().getId());
                    sendMessageToUser(answer, a.getUser(), user);
                });

         */
    }

    private void onTest(Message message) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId());
        sendMessageRequest.setReplyToMessageId(message.getMessageId());
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        sendMessageRequest.setReplyMarkup(forceReplyKeyboard);
        sendMessageRequest.setText("Пожалуйста, ответьте на это сообщение, указав пункт назначения.");

        try {
            executeAsync(sendMessageRequest, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> method, Message sentMessage) {
                    if (sentMessage != null) {

                        User userFrom = message.getFrom();
                        Integer sendMessageId = sentMessage.getMessageId();
                        String messageText = message.getText();
                        String sendMessageText = sentMessage.getText();
                        /*
                        DatabaseManager.getInstance().addUserForDirection(message.getFrom().getId(), WATING_DESTINY_STATUS,
                                sentMessage.getMessageId(), message.getText());

                         */
                    }
                }

                @Override
                public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
                }

                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                }
            });
        } catch (TelegramApiException e) {
            log.error("onTest", e);
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
                botUser.isIslogin() == false) {

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
        String name = new StringBuilder()
                .append(user.getFirstName())
                .append(" ")
                .append(user.getLastName())
                .toString();
        return name;
    }
}
