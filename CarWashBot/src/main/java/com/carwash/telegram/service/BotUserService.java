package com.carwash.telegram.service;

import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.exception.ItemNotFoundException;
import com.carwash.telegram.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@NoArgsConstructor
public class BotUserService {

    private final BotUserRepository repository;

    /***
     * Получить пользователя по name
     * в таблице сервисов бота
     * Если пользователя с именем name нет - будет исключение ItemNotFoundException
     * @param id - имя пользователя
     * @return BotUser - пользователь с именем id
     */
    public BotUser findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("BotUser not found, id = " + id));
    }

    /***
     * Получить пользователя по name
     * в таблице сервисов бота
     * Если пользователя с именем name нет - будет добавлен
     * @param id - имя пользователя
     * @return BotUser - пользователь с именем id
     */
    public BotUser getBotUser(String id) {
        BotUser botUserNew;
        Optional<BotUser> botUser = repository.findById(id);
        if (!botUser.isPresent()) {
            botUserNew = new BotUser();
            botUserNew.setId(id);
            botUserNew.setIslogin(false);
            botUserNew = repository.save(botUserNew);
        } else {
            botUserNew =  botUser.get();
        }
        return botUserNew;
    }

    /***
     * Для пользователя с именем name
     * в таблице сервисов бота
     * установить шаг сервиса = step
     * @param name - имя пользователя
     */
    public void setServiceStep(String name, BotUserStepService step) {
        BotUser botUser = getBotUser(name);
        botUser.setStepService(step);
        /*
        if (step == BotUserStepService.NONE) {
            botUser.setIslogin(false);
        }
        */
        repository.save(botUser);
    }

    /***
    * Для пользователя с именем name
    * получить текущий шаг сервиса
    * @param name - имя пользователя
    * @return BotUserStepService - шаг сервиса
    */
    public BotUserStepService getServiceStep(String name) {
        BotUser botUser = getBotUser(name);
        return botUser.getStepService();
    }

    /***
     * Создать нового пользователя
     * в таблице сервисов бота
     * @param botUser - данные пользователя
     * @return BotUser - данные созданного пользователя
     */
    public BotUser save(BotUser botUser) {
        return repository.save(botUser);
    }


    /***
     * Обновить данные пользователя
     * в таблице сервисов бота
     * @param botUser - данные пользователя
     * @return BotUser - обновленные данные пользователя
     */
    public BotUser update(BotUser botUser) {
        findById(botUser.getId());
        return repository.save(botUser);
    }

    /***
     * Удалить данные пользователя
     * в таблице сервисов бота
     * @param id - имя пользователя
     */
    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }
}
