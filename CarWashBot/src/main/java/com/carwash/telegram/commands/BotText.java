package com.carwash.telegram.commands;

public class BotText {

    private static final StringBuilder stringBuilder = new StringBuilder("")
            .append("Бот осуществялет запись на мойку машин. \n")
            .append("Бот поддерживает следующие комманды: \n")
            .append("\n")
            .append("/start - начало работы с ботом \n")
            .append("/help - список команд \n")
            .append("\n")
            .append("Идентификация\n")
            .append("/login - вход по логину и паролю (имени и номеру телефона) \n")
            .append("/register - регистрация \n")
            .append("\n")
            .append("Информация\n")
            .append("/allcity - список городов \n")
            .append("/allcarwash - список моек в Вашем городе \n")
            .append("/neacarwash - список ближайших моек \n")
            .append("/status - статус заказа \n")
            .append("/history - история всех Ваших заказов \n")
            .append("\n")
            .append("Заказ\n")
            .append("/orderon - запись на мойку \n")
            .append("/orderoff - обнуление заказа \n");
    public static final String HELP_TEXT = stringBuilder.toString();

    //регистрация
    public static final String LOGIN_TEXT = "Введите номер телефона";
    public static final String SELECT_CITY = "Выберите Ваш город";
    public static final String ANOTHER_CITY = "Другой";

    //-----------------------------

    public static final String SIGN_UP_TEXT = "Вы хотите зарегистриваться?";

    public static final String YES_BUTTON = "YES_BUTTON";

    public static final String NO_BUTTON = "NO_BUTTON";

    public static final String SIGN_UP_REG_TEXT = "Для регистрации на сайте введите свой номер";

    public static final String SIGN_UP_NO_REG_TEXT = "Жаль что Вы передумали. Будем рады видеть Вас снова";

    public static final String COMMAND_NOT_RECOGNIZE = "Извини, команда не распознана!";

    public static final String ORDER_WONT_TEXT = "Вы хотите сделать заказ на выбранной автомойке?";

    public static final String ORDER_COORD_TEXT = "Введите Ваши координаты через пробел";

    public static final String SIGN_ORDER_TEXT = "Заказ успешно создан";
}
