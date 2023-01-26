package com.carwash.telegram.core;

import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {

    private final RestTemplate restTemplate;

    /***
     * Вход пользователя в сервисе по имени и номеру телефона
     * @param userModel - dto пользователя ((имя, телефон)
     * @return id пользователя, если пользователь зарегистрирован в сервисе, 0 - если нет
     */
    @SneakyThrows
    public HttpAnswer login(@RequestBody BotUserDto userModel) {

        String url = "http://localhost:8083/api/v1/admin-service/userInfo/login/user";
//        String url = "http://localhost:8080/api/v1/admin-service/userInfo/login/user";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url, new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                if (response.getBody() != null) {
                    httpAnswer.setID(response.getBody().getId());
                }
            }

        } catch (Exception ex) {
            log.info("Exception = " + ex);
        }

        return httpAnswer;
    }

    /***
     * Регистрация пользователя в сервисе (имя, телефон, id города)
     * @param userModel - dto пользователя
     * @return id пользователя, если пользователь зарегистрирован в сервисе, 0 - если нет
     */
    @SneakyThrows
    public HttpAnswer register(@RequestBody BotUserDto userModel) {

        URL url = new URL("http://localhost:8083/api/v1/admin-service/userInfo/register/user");
//        URL url = new URL("http://localhost:8080/api/v1/admin-service/userInfo/register/user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url.toURI(), new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {

                if ( response.getBody() != null) {
                    httpAnswer.setID(response.getBody().getId());
                }
            }

        } catch (Exception ex) {
            log.info("Exception = " + ex);
        }

        return httpAnswer;
    }

    /***
     * Получение списка всх городов
     *
     * @return список городо
     */
    @SneakyThrows
    public HttpAnswer getAllCity() {

        URL url = new URL("http://localhost:8083/api/v1/admin-service/city");
//        URL url = new URL("http://localhost:8080/api/v1/admin-service/city");

        CityDto[] allCityDtos;

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<CityDto[]> response =
                    restTemplate.getForEntity(
                            url.toURI(),
                            CityDto[].class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {
                if ( response.getBody() != null) {
                    allCityDtos = response.getBody();
                    httpAnswer.setObjectList(List.of(allCityDtos));
                }
            }

        } catch (Exception ex) {
            log.error("Exception " + ex);
        }

        return httpAnswer;
    }

    /***
     * Получить список всех моек,
     * находящихсы в городе, в котором зарегистрирован пользователь
     * у которых есть незанятое время в выбранную пользователем дату
     * @param idUser - id пользователя
     * @param date - выбранная дата
     * @return - список свободных моек в городе пользователя
     */
    @SneakyThrows
    public HttpAnswer getAllCarWash(Long idUser, String date) {

        String url = "http://localhost:8080/api/v1/CarWash-service/carWash/all-for-user-on-date?idUser={idUser}&date={date}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,String> param = new HashMap<>();
        param.put("idUser", String.valueOf(idUser));
        param.put("date",date);

        HttpEntity<CarWashDto[]> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<CarWashDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarWashDto[].class, param);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }

    /***
     * Получить список всех моек,
     * находящихсы в городе, в котором зарегистрирован пользователь
     * в максимальной близости заданного координатами местонахождения
     * у которых есть незанятое время в выбранную пользователем дату
     * @param idUser - id пользователя
     * @param date - выбранная дата
     * @param latitude - широта
     * @param longitude - долгота
     * @return - список близлежащих моек, у которых есть незанятое время в заданную дату
     */
    @SneakyThrows
    public HttpAnswer getNewCarWash(Long idUser, String date, String latitude, String longitude) {

        String url = "http://localhost:8080/api/v1/CarWash-service/carWash/near-for-user-on-date?idUser={idUser}&date={date}&latitude={latitude}&longitude={longitude}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,String> param = new HashMap<>();
        param.put("idUser", String.valueOf(idUser));
        param.put("date",date);
        param.put("latitude", latitude);
        param.put("longitude",longitude);

        HttpEntity<CarWashDto[]> requestEntity = new HttpEntity<>(null,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<CarWashDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarWashDto[].class, param);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }

    /***
     * Получить список всех свободных позиций времени для записи пользователя на автомойку
     * для выбранной пользователем даты, мойки
     * @param date - выбранная дата
     * @param idCarWash - id выбранной мойки
     * @return - список свободных позиций времени
     */
    @SneakyThrows
    public HttpAnswer getAllTime(String date, Long idCarWash ) {

        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/byDate?date={date}&idCarWash={idCarWash}";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableDto[]> requestEntity = new HttpEntity<>(null,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableDto[].class, date, idCarWash);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }

    /***
     * Оформление заказа пользователдя на мойку машины
     * @param timeTableDto - данные заказа
     * @return true - заказ оформлен успешно
     */
    @SneakyThrows
    public HttpAnswer orderOn(@RequestBody TimeTableDto timeTableDto) {
        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/order-on";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<TimeTableDto> requestEntity = new HttpEntity<>(timeTableDto, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, TimeTableDto[].class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {
                log.info("successful");
            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }

    /***
     * Получить список всех активных заказов пользователя
     * (дата >= текущей )
     * @param idUser - id пользователя
     * @return - список активных заказов пользователя
     */
    @SneakyThrows
    public HttpAnswer getListOrderOn(Long idUser ) {

        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/history-on?idUser={idUser}";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableDto[]> requestEntity = new HttpEntity<>(null,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableDto[].class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }

    /***
     * Получить список запоанированных заказов пользователя
     * (дата >= текущей, статус - запланировано )
     * @param idUser - id пользователя
     * @return - список активных заказов пользователя
     */
    @SneakyThrows
    public HttpAnswer getListOrderOff(Long idUser ) {

        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/list-order-off?idUser={idUser}";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableDto[]> requestEntity = new HttpEntity<>(null,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableDto[].class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }

    /***
     * Получить историю всех заказов пользователя
     * (дата >= текущей )
     * @param idUser - id пользователя
     * @return - список активных заказов пользователя
     */
    @SneakyThrows
    public HttpAnswer getHistory(Long idUser ) {

        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/history?idUser={idUser}";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableDto[]> requestEntity = new HttpEntity<>(null,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableDto[].class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if (response.getBody() != null) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }


}
