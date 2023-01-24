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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class RegisterSelectCityCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public RegisterSelectCityCommand(BotUserService botUserService,
                                     BotController botController) {
        super("register_select_city", "Выбор города для регистрации\n", botUserService, botController);
    }

    /**
     * обработка сообщения, введенного пользователем
     * @param absSender - отправляет ответ пользователю
     * @param update - сообщение
     * @param user - пользователь, который выполнил команду
     * @param botUser - данные, связанные с действиями пользователя
     */
    public void execute(AbsSender absSender, Update update, User user, BotUser botUser) {

        String callbackData = update.getCallbackQuery().getData();

        SendMessage answer = new SendMessage();
        answer.setChatId(update.getCallbackQuery().getMessage().getChat().getId());

        if (callbackData.equals(BotText.ANOTHER_CITY)) {

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);

            answer.setText(BotText.ANOTHER_CITY_NO_WASH);

            execute(absSender, answer, user);

        } else {
            try {
                    /*
                    String[] parts = callbackData.split(" ");
                    String sIdCity = parts[0];
                    String city = parts[1];
                    Long idCity = Long.valueOf(sIdCity);

                    botUser.setIdCity(idCity);
                     */

                botUser.setIdCity(Long.valueOf(callbackData));
                botUser = botUserService.update(botUser);

                BotUserDto userModel = new BotUserDto();
                userModel.setName(botUser.getId());
                userModel.setPhone(botUser.getPhone());
                userModel.setIdCity(botUser.getIdCity());

                    /*
                    CityDto cityDto = new CityDto();
                    cityDto.setId(botUser.getIdCity());
                    //cityDto.setName(city);

                    userModel.setCity(cityDto);
                    */

                StringBuilder sb = new StringBuilder();

                HttpAnswer httpAnswer = botController.register(userModel);

                if (httpAnswer.isSuccess()) {
                    sb.append(BotText.REGISTER_SUCCEESS);
                    botUser.setIslogin(true);
                    botUser.setIdUser(httpAnswer.getID());

                } else {
                    sb.append(httpAnswer.getStatus());
                }

                botUser.setStepService(BotUserStepService.NONE);
                botUserService.save(botUser);

                answer.setText(sb.toString());
                execute(absSender, answer, user);

            } catch (Exception ex) {
                answer.setText(BotText.UNKNOWN_ERROR);
                execute(absSender, answer, user);
            }

        }
    }


}