package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.BotUserDto;
import com.carwash.telegram.entity.dto.CityDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.service.BotUserService;
import com.carwash.telegram.util.UncheckedConversion;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class RegisterInputPhone extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public RegisterInputPhone(BotUserService botUserService,
                              BotController botController) {
        super("login_input_phone", "ввод номера телефона для входа\n", botUserService, botController);
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

        String phone = text;
        if (!ValidatePhone(phone)) {
            log.info("User {} is trying to set empty phone.", user.getId());
            message.setText(BotText.INPUT_PHONE);
            execute(absSender, message, user);
            return;
        }

        botUser.setPhone(phone);
        botUser = botUserService.update(botUser);

        // проверить, что пользователь еще не регистрировался в системе.
        // если регистрировался - попробовать залогиниться
        BotUserDto userModel = new BotUserDto();
        userModel.setName(botUser.getId());
        userModel.setPhone(botUser.getPhone());

        HttpAnswer httpAnswer = botController.login(userModel);

        if (httpAnswer.isSuccess()) {
            message.setText(BotText.LOGIN_SUCCESS);
            execute(absSender, message, user);

            botUser.setIslogin(true);
            botUser.setIdUser(httpAnswer.getID());
            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);

            return;
        }

        HttpAnswer httpAnswer1 = botController.getAllCity();
        if (!httpAnswer1.isSuccess()) {
            message.setText(httpAnswer1.getStatus());
            execute(absSender, message, user);

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);

            return;
        }

        List rawList = httpAnswer1.getObjectList();
        List<CityDto> allCity = UncheckedConversion.castList(CityDto.class, rawList);

        message = getSelectCommandButton(chat,allCity);
        execute(absSender, message, user);

        botUser.setStepService(BotUserStepService.REGISTRY_SELECT_CITY);
        botUserService.save(botUser);
    }

    /**
     * Проверка введеного номера телефона на соответствие шаблону
     * @param text - текст сообщения, должен содержать только номер телефона в формате X-XXX-XXX-XX-XX
     * @return - true, если проверка успешна
     */
    private boolean ValidatePhone(String text) {

        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d-\\d{3}-\\d{3}-\\d{2}-\\d{2}$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        }

        return false;
    }

    public SendMessage getSelectCommandButton(Chat chat, List<CityDto> allCity) {
        SendMessage message = new SendMessage();
        long chatId = chat.getId();
        message.setChatId(String.valueOf(chatId));
        message.setText(BotText.SELECT_CITY);

        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboard(allCity);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<CityDto> allCity) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;
        InlineKeyboardButton btn;

        for (CityDto city: allCity) {
            rowInLine = new ArrayList<>();
            btn =  new InlineKeyboardButton();
            btn.setText(city.getName());
            //btn.setCallbackData(city.getId().toString() + " " + city.getName());
            btn.setCallbackData(city.getId().toString());
            rowInLine.add(btn);
            rowsInLine.add(rowInLine);
        }

        rowInLine = new ArrayList<>();
        btn =  new InlineKeyboardButton();
        btn.setText(BotText.ANOTHER_CITY);
        btn.setCallbackData(BotText.ANOTHER_CITY);
        rowInLine.add(btn);
        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

}