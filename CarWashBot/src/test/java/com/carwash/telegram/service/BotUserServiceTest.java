package com.carwash.telegram.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.carwash.telegram.entity.BotUser;
import com.carwash.telegram.entity.enums.BotUserStepService;
import com.carwash.telegram.repository.BotUserRepository;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotUserServiceTest {

    @Mock
    private BotUserRepository repository;

    private BotUserService botUserService;

    @BeforeEach
    void setUp() {
        botUserService = new BotUserService(repository);
    }

    @Test
    void getBotUserTest() {

        String name = "Иван Иванов";
        BotUser botUser = new BotUser();
        botUser.setId(name);
        botUser.setIslogin(false);

        when(repository.findById(name)).thenReturn(Optional.of(botUser));

        BotUser botUser1 = botUserService.getBotUser(name);
        assertThat(botUser1).isNotNull().isEqualTo(botUser);
    }

    @Test
    void setServiceStepTest() {

        String name = "Иван Иванов";
        BotUser botUser = new BotUser();
        botUser.setId(name);

        when(repository.save(botUser)).thenReturn(botUser);
        when(repository.findById(name)).thenReturn(Optional.of(botUser));

        botUserService.setServiceStep(name,BotUserStepService.ORDER_ON_NEAR_OR_ALL);

        assertThat(botUserService.getServiceStep(name)).isNotNull();
        Assertions.assertEquals(botUserService.getServiceStep(name), BotUserStepService.ORDER_ON_NEAR_OR_ALL);
    }

    @Test
    void saveTest() {

        String name = "Иван Иванов";
        BotUser botUser = new BotUser();
        botUser.setId(name);

        when(repository.save(botUser)).thenReturn(botUser);

        BotUser savedBotUser =  botUserService.save(botUser);
        assertThat(savedBotUser.getId()).isNotNull();
    }

    @Test
    void updateTest() {

        String name = "Иван Иванов";
        BotUser botUser = new BotUser();
        botUser.setId(name);
        botUser.setStepService(BotUserStepService.NONE);

        when(repository.findById(anyString())).thenReturn(Optional.of(botUser));
        when(repository.save(botUser)).thenReturn(botUser);

        BotUser savedBotUser = botUserService.update(botUser);
        botUserService.setServiceStep(name, BotUserStepService.REGISTRY_SELECT_CITY);
        assertThat(savedBotUser.getId()).isNotNull();
    }

    @Test
    void save() {
        String name = "Иван Иванов";
        BotUser botUser = botUserService.getBotUser(name);
        if (botUser!=null) {
            botUserService.setServiceStep(name, BotUserStepService.REGISTRY_SELECT_CITY);
        }

        when(repository.save(botUser)).thenReturn(botUser);

        BotUser savedBotUser = botUserService.save(botUser);
        assertThat(savedBotUser.getStepService()).isNotNull();

    }

}
