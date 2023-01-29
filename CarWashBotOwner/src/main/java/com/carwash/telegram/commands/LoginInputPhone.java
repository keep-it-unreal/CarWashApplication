package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.BotUserDto;
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
public final class LoginInputPhone extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public LoginInputPhone(BotUserService botUserService,
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

        StringBuilder sb = new StringBuilder();

        botUser.setPhone(phone);
        botUser = botUserService.update(botUser);

        BotUserDto userModel = new BotUserDto();
        userModel.setName(botUser.getId());
        userModel.setPhone(botUser.getPhone());

        HttpAnswer httpAnswer = botController.login(userModel);
        if (httpAnswer.isSuccess()) {
            sb.append(BotText.LOGIN_SUCCESS);
            botUser.setIslogin(true);
            userModel = (BotUserDto) httpAnswer.getObject();
            botUser.setIdUser(userModel.getId());
            botUser.setIdCity(userModel.getIdCity());

        } else {
            sb.append(BotText.LOGIN_ERROR);
            botUser.setIslogin(false);
        }

        botUser.setStepService(BotUserStepService.NONE);
        botUserService.save(botUser);

        message.setText(sb.toString());
        execute(absSender, message, user);
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
}