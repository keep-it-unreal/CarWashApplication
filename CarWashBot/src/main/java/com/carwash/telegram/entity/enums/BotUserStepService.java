package com.carwash.telegram.entity.enums;

public enum BotUserStepService {
    NONE,                       //0
    LOGIN,                      //1
    REGISTER,                   //2
    REGISTRY_SELECT_CITY,       //3
    NEAR_CAR_WASH,              //4
    ORDER_ON_INPUT_DATE,        //5
    ORDER_ON_NEAR_OR_ALL,       //6
    ORDER_ON_SELECT_CAR_WASH,   //7
    ORDER_ON_SELECT_TIME,       //8
    LIST_ORDER_ON,              //9
    ORDER_OFF_SELECT_ORDER,     //10


    CAR_WASH_LIST,              //4
    CAR_WASH_LIST_ADD,          //5
    CAR_WASH_LIST_ADD_ADDRESS,  //6
    CAR_WASH_LIST_ADD_COORD,    //7
    CAR_WASH_LIST_ADD_BEGIN_END,    //8
    CAR_WASH_ONE,               //9
    TIME_SUM,                   //10
    TIME_SUM_LIST,              //11
    CAR_WASH_UPDATE,            //12
    TIME_LIST_ORDER;            //13


    BotUserStepService() {
    }

}
