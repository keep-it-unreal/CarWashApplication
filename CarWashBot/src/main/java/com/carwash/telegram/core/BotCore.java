package com.carwash.telegram.core;

import com.carwash.telegram.commands.BotText;
import com.carwash.telegram.config.BotConfig;
import com.carwash.telegram.model.AllCarWashModel;
import com.carwash.telegram.model.BotUserModel;
import com.carwash.telegram.model.CityModel;
import com.carwash.telegram.model.NeaCarWashModel;
import com.carwash.telegram.service.BotButtonService;
import com.carwash.telegram.service.BotMenuService;
import com.carwash.telegram.service.EditMessageService;
import com.carwash.telegram.service.SendMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class BotCore extends TelegramLongPollingBot {

    private final BotConfig config;
    private final SendMessageService sendMessageService = new SendMessageService();
    private final EditMessageService editMessageService = new EditMessageService();
    private final BotButtonService botButtonService = new BotButtonService();
    private final BotController botController;

    private boolean isLogin = false;
    private boolean isRegisterStep1 = false;
    private boolean isRegisterStep2 = false;
    private boolean isNeaCarWash = false;
    private boolean isOrderOn = false;
    private Long carWashId;

    //user
    private Long userId;
    private String phone;

    List<NeaCarWashModel> neaCarWash;
    List<AllCarWashModel> allCarWash;
    List<CityModel> allCity;

    private void clearMainFlag() {
        isLogin = false;
        isRegisterStep1 = false;
        isRegisterStep2 = false;
        isNeaCarWash = false;
        isOrderOn = false;
        carWashId = 0L;
        userId = 0L;
        phone = "";
    }

    public BotCore(BotConfig config, BotController botController) {
        this.config = config;
        this.botController = botController;
        BotMenuService menu = new BotMenuService();
        try {
            this.execute(new SetMyCommands(menu.getListOfCommand(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            switch (message) {
                case "/start" :
                    clearMainFlag();
                    String name = update.getMessage().getChat().getFirstName();
                    String answerText = "Привет, " + name + " рад видеть тебя!";
                    executeMessage(sendMessageService.createTextMessage(update, answerText));
                    break;
                case "/help" :
                    String helpText = BotText.HELP_TEXT;
                    executeMessage(sendMessageService.createTextMessage(update, helpText));
                    break;
                case "/login" :
                    clearMainFlag();
                    isLogin = true;
                    executeMessage(sendMessageService.createTextMessage(update, BotText.LOGIN_TEXT));
                    break;
                case "/register" :
                    clearMainFlag();
                    isRegisterStep1 = true;
                    //executeMessage(botButtonService.getSignUpCommandButton(update));
                    executeMessage(sendMessageService.createTextMessage(update, BotText.LOGIN_TEXT));
                    break;
                case "/allcity" :
                    allCity = botController.getAllCity();
                    executeMessage(sendMessageService.createTextMessageListCity(update, allCity));
                    break;
                case "/neacarwash" :
                    isNeaCarWash = true;
                    executeMessage(sendMessageService.createTextMessage(update, BotText.ORDER_COORD_TEXT));
                    break;
                case "/allcarwash" :
                    allCarWash = botController.getAllCarWash();
                    executeMessage(sendMessageService.createTextMessageListCarWash(update, allCarWash));
                    break;
                default:

                    String commandNotRecognize = BotText.COMMAND_NOT_RECOGNIZE;

                    if (isLogin && update.hasMessage() && update.getMessage().hasText()) {
                        System.out.println(update.getMessage().getText());
                        BotUserModel userModel = new BotUserModel();
                        userModel.setName(update.getMessage().getChat().getFirstName());
                        userModel.setPhone(update.getMessage().getText());
                        String text = botController.login(userModel);
                        userId = botController.getUserSignUpId();
                        SendMessage textMessage = sendMessageService.createTextMessage(update, text);
                        executeMessage(textMessage);
                        isLogin = false;

                    } else if (isRegisterStep1 && update.hasMessage() && update.getMessage().hasText()) {
                        phone = update.getMessage().getText();
                        allCity = botController.getAllCity();
                        executeMessage(botButtonService.getSelectCityCommandButton(update,allCity));
                        isRegisterStep1 = false;
                        isRegisterStep2 = true;

                        /*
                    } else if (isRegister && update.hasMessage() && update.getMessage().hasText()) {
                        System.out.println(update.getMessage().getText());
                        BotUserModel userModel = new BotUserModel();
                        userModel.setName(update.getMessage().getChat().getFirstName());
                        userModel.setPhone(update.getMessage().getText());
                        SendMessage textMessage = sendMessageService.createTextMessage(update, botController.signUp(userModel));
                        executeMessage(textMessage);
                        isRegister = false;
                        */

                    } else {
                        String text = update.getMessage().getText();
                        boolean result = text.matches("(\\/*)\\d");
                        if (isNeaCarWash && result) {
                            isOrderOn = true;
                            carWashId = Long.valueOf(text.substring(1));
                            executeMessage(botButtonService.getOrderCommandButton(update));
                        } else if (isNeaCarWash && update.hasMessage() && update.getMessage().hasText()) {
                            String[] parts = text.split(" ");
                            String lattid = parts[0];
                            String longt = parts[1];

                            neaCarWash = botController.getNeaCarWash(lattid, longt);
                            executeMessage(sendMessageService.createTextMessageListCarWash(update, neaCarWash));
                        } else {
                            executeMessage(sendMessageService.createTextMessage(update, commandNotRecognize));
                        }
                    }
            }

            //выбираем город для регистрации
        } else if (isRegisterStep2 && update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            /* если выбрали Другой -
                дать ввести название города,
                отправить запрос на его добавление в справочник
                получить id города
            */
            if (callbackData.equals(BotText.ANOTHER_CITY)) {
                String signUpText = BotText.SIGN_UP_REG_TEXT;
                executeMessage(editMessageService.editTextMessage(update, signUpText));

                // в callbackData idCity в формате строки
            } else {

            }
            /*
            } else if (callbackData.equals(BotText.NO_BUTTON)) {
                String text = BotText.SIGN_UP_NO_REG_TEXT;
                executeMessage(editMessageService.editTextMessage(update, text));
            }
            */
        } else if (isOrderOn && update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            if (callbackData.equals(BotText.YES_BUTTON)) {
                SendMessage textMessage = sendMessageService.createCallbackQuaryTextMessage(botController.createOrder(carWashId), update);
                executeMessage(textMessage);
                isNeaCarWash = false;
                isOrderOn = false;
            } else if (callbackData.equals(BotText.NO_BUTTON)) {
                String text = BotText.SIGN_UP_NO_REG_TEXT;
                executeMessage(editMessageService.editTextMessage(update, text));
            }
        }
    }

    private <T extends BotApiMethod> void executeMessage(T message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
