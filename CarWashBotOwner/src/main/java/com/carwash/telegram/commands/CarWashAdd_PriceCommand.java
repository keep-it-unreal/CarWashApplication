package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.CarWashForOwnerDto;
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
public final class CarWashAdd_PriceCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public CarWashAdd_PriceCommand(BotUserService botUserService,
                                   BotController botController) {
        super("add-price", "для добавляемой автомойки ввести стоимость\n", botUserService, botController);
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

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        String price = text;
        if (!ValidatePrice(price)) {
            log.info("User {} is trying to set empty phone.", user.getId());
            answer.setText(BotText.ADD_INPUT_PRICE);
            execute(absSender, answer, user);
            return;
        }

        botUser.setPrice(Double.parseDouble(price));
        botUser = botUserService.update(botUser);

        CarWashForOwnerDto carWashForOwnerDto = new CarWashForOwnerDto();
        carWashForOwnerDto.setAddress(botUser.getAddress());
        carWashForOwnerDto.setLatitude(botUser.getLatitude());
        carWashForOwnerDto.setLongitude(botUser.getLongitude());
        carWashForOwnerDto.setDailyStartTime(botUser.getDailyStartTime());
        carWashForOwnerDto.setDailyEndTime(botUser.getDailyEndTime());
        carWashForOwnerDto.setPrice(botUser.getPrice());
        carWashForOwnerDto.setIdUser(botUser.getIdUser());
        carWashForOwnerDto.setIdCity(botUser.getIdCity());

        HttpAnswer httpAnswer;

        if (botUser.getPrevStepService() == BotUserStepService.CAR_WASH_UPDATE) {
            botUser.setPrevStepService(BotUserStepService.NONE);
            botUserService.update(botUser);
            carWashForOwnerDto.setId(botUser.getIdCarWash());

            httpAnswer = botController.carWashUpdate(botUser.getIdCarWash(), carWashForOwnerDto);

        } else {
            httpAnswer = botController.carWashAdd(carWashForOwnerDto);
        }

        if (httpAnswer.isSuccess()) {
            answer.setText(httpAnswer.getStatus());
            botUser.setIdCarWash(httpAnswer.getID());

        } else {
            answer.setText(httpAnswer.getStatus());
            botUser.setIdCarWash(null);
        }

        botUser.setStepService(BotUserStepService.NONE);
        botUserService.save(botUser);

        CarWashListCommand carWashListCommand = new CarWashListCommand(botUserService, botController);
        carWashListCommand.execute(absSender, user, chat, new String[0]);
    }

    /**
     * Проверка введеной стоимости на соответствие шаблону
     * @param text - текст сообщения, должен содержать стоимость в формате ххх.хх
     * @return - true, если проверка успешна
     */
    public boolean ValidatePrice(String text) {
        if (text == null || text.equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d+(\\.\\d{2})?$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        }
        return false;
    }



}