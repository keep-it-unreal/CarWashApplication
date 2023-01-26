package com.carwash.telegram.core;

public class BotText {

    // общее
    public static final String UNKNOWN_COMMAND = "Неизвестная команда!";
    public static final String EMPTY_MESSAGE = "Сообщение не должно быть пустым!";
    public static final String LOGIN_OR_REGISTRY = "Пожалуйста, войдите в систему /login или зарегистрируйтесь /registry";
    public static final String UNKNOWN_ERROR = "Неизвестная ошибка";

    // вход
    public static final String INPUT_PHONE = "Введите свой номер телефона в формате X-XXX-XXX-XX-XX";
    public static final String LOGIN_SUCCESS = "Вы успешно подключились к сервису";
    public static final String LOGIN_ERROR = "Учетная запись не найдена";

    // регистрация
    public static final String SELECT_CITY = "Выберите Ваш город";
    public static final String ANOTHER_CITY = "Другой";
    public static final String ANOTHER_CITY_NO_WASH = "В сервисе не зарегистрированы автомойки в других городах";
    public static final String REGISTER_SUCCEESS = "Вы успешно зарегистрированы в сервисе";

    // запись на сервис мойки
    public static final String INPUT_DATE = "Введите дату, большую, чем сегодня, в формате DD.MM.YYYY";
    public static final String LIST_NEAR_OR_ALL_CAR_WASH = "Вывести список всех моек или ближайших?";
    public static final String SELECT_ALL = "Все автомойки";
    public static final String LIST_CAR_WASH_EMPTY = "К сожалению, нет моек, свободных в указанную Вами дату";
    public static final String SELECT_NEAR = "Ближайшие автомойки";
    public static final String ORDER_COORD_TEXT = "Введите Ваши координаты через пробел";
    public static final String ORDER_COORD_BTN = "Получить координаты местонахождения";
    public static final String SELECT_CAR_WASH = "Выберите автомойку";
    public static final String CANCEL = "Отказаться от выбора";
    public static final String CANCEL_USER = "Операция прервана пользователем";

    public static final String SELECT_TIME = "Выберите удобное для Вас время";

    public static final String ORDER_ON_SUCCESS = "Заказ на мойку машины оформлен успешно";
    public static final String ORDER_ON_ERROR = "При оформлении заказа произошел сбой. Примите наши извинения";

    // просмотр активных заказов
    public static final String LIST_ACTIVE_ORDER = "Список Ваших активных заказов";
    public static final String LIST_ACTIVE_ORDER_EMPTY = "У Вас нет активных заказов";

    // отказ от заказа
    public static final String ORDER_OFF_SUCCESS = "Заказ на мойку машины отменен";
    public static final String ORDER_OFF_ERROR = "При отмене заказа произошел сбой. Примите наши извинения";

    // история заказов
    public static final String HISTORY = "История Ваших заказов";
    public static final String HISTORY_EMPTY = "У Вас не было заказов";

}
