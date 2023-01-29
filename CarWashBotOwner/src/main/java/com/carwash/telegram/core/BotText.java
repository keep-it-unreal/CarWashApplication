package com.carwash.telegram.core;

public class BotText {

    // общее
    public static final String UNKNOWN_COMMAND = "Неизвестная команда!";
    public static final String EMPTY_MESSAGE = "Сообщение не должно быть пустым!";
    public static final String LOGIN_OR_REGISTRY = "Пожалуйста, войдите в систему /login или зарегистрируйтесь /registry";
    public static final String UNKNOWN_ERROR = "Неизвестная ошибка";
    public static final String EMPTY = "Данные не найдены";

    // вход
    public static final String INPUT_PHONE = "Введите свой номер телефона в формате X-XXX-XXX-XX-XX";
    public static final String LOGIN_SUCCESS = "Вы успешно подключились к сервису";
    public static final String LOGIN_ERROR = "Учетная запись не найдена";

    // регистрация
    public static final String SELECT_CITY = "Выберите Ваш город";
    public static final String ANOTHER_CITY = "Другой";
    public static final String ANOTHER_CITY_NO_WASH = "В сервисе не зарегистрированы автомойки в других городах";
    public static final String REGISTER_SUCCEESS = "Вы успешно зарегистрированы в сервисе";

    // список моек
    public static final String LIST_CARWASH = "Список Ваших моек: \n<i>Вы можете выполнить действие (кнопки под списком)\nили выбрать мойку, а потомы выбрать действие уже для нее</i>";
    public static final String BTN_NSERT = "Добавить мойку";
    public static final String BTN_ADD_TIME_CUR = "+ график на этот месяц";
    public static final String BTN_ADD_TIME_NEXT = "+ график на след. месяц";
    public static final String BTN_LIST_TIME_LAST = "Отчет за период";

    // суммарный отет за период
    public static final String TIME_SUM_PERIOD = "Введите через пробел даты отчетного периода\nв формате dd.mm.yyyy dd.mm.yyyy";
    public static final String TIME_SUM_LIST = "Итого за период было обслужено клиентов, итоговая сумма";

    // мойка byID
    public static final String BTN_UPDATE = "Изменить";
    public static final String BTN_DELETE = "Удалить";
    public static final String BTN_WEEKEND = "Выходные";
    public static final String BTN_TIME_NOW = "Заказы на сегодня";
    public static final String BTN_TIME_BY_DATE = "Заказы на дату";
    public static final String BTN_CANCEL_TIME_NOW = "Отменить заказы на сегодня";
    public static final String BTN_CANCEL_TIME_BY_DATE = "Отменить заказы на дату";

    // добавить мойку
    public static final String ADD_INPUT_ADDRESS = "Введите адрес мойки";
    public static final String ADD_INPUT_COORD = "Через пробел введите координаты мойки \nв формате xx.xxxxx xx.xxxxx";
    public static final String ADD_INPUT_BEGIN_END = "Через пробел введите часы начала-окончания работы мойки\n в формате xx xx\nДля круглосуточной мойки введите 00 24";
    public static final String ADD_INPUT_PRICE = "Введите стоимость услуги в формате xxx.xx";

    // заказы на дату
    public static final String DATE_ORDER = "Введите дату в формате dd.mm.yyyy";
    public static final String TIME_TABLE_LISTt = "Список заказов на заданную дату";



}
