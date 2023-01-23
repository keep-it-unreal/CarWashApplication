package com.carwash.telegram.entity.enums;

public enum BotUserStepService {
    NONE(0),
    LOGIN(1),
    REGISTER(2),
    REGISTRY_SELECT_CITY(3),
    REGISTRY_ANOTHER_CITY(4),
    NEAR_CAR_WASH(5),
    ORDER_ON_INPUT_DATE(6),
    ORDER_ON_NEAR_OR_ALL(7),
    ORDER_ON_SELECT_CAR_WASH(8),
    ORDER_ON_SELECT_TIME(9);


    BotUserStepService(int i) {
    }

}
