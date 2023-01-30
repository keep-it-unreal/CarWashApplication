package com.carwash.telegram.commands;

import com.carwash.telegram.core.BotController;
import com.carwash.telegram.core.BotText;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.TimeTableDto;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.entity.enums.StatusFree;
import com.carwash.telegram.entity.enums.StatusWork;
import com.carwash.telegram.service.BotUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public final class OrderOffSelectOrdreCommand extends AnswerCommand {

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public OrderOffSelectOrdreCommand(BotUserService botUserService,
                                      BotController botController) {
        super("order_off_select_order", "Выбор заказа для отмены\n", botUserService, botController);
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

        if (callbackData.equals(BotText.CANCEL)) {

            botUser.setStepService(BotUserStepService.NONE);
            botUserService.save(botUser);

            answer.setText(BotText.CANCEL_USER);

            execute(absSender, answer, user);

            return;

        }

        botUser.setDateTable(callbackData);
        botUser = botUserService.update(botUser);

        TimeTableDto timeTableDto = new TimeTableDto();
        timeTableDto.setDateTable(botUser.getDateTable());
        timeTableDto.setIdCarWash(botUser.getIdCarWash());
        //timeTableDto.setIdUser(null);
        timeTableDto.setStatusFree(StatusFree.FREE);
        timeTableDto.setStatusWork(StatusWork.CANCEL_USER);

        HttpAnswer httpAnswer = botController.orderOn(timeTableDto);

        if (httpAnswer.isSuccess()) {
            answer.setText(BotText.ORDER_OFF_SUCCESS);

        } else {
            answer.setText(BotText.ORDER_OFF_ERROR);
        }

        botUser.setStepService(BotUserStepService.NONE);
        botUserService.save(botUser);

        execute(absSender, answer, user);

    }

}