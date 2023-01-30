package com.carwash.telegram.core;

import com.carwash.telegram.config.BotConfig;
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

    private final BotConfig botConfig;
    private final RestTemplate restTemplate;


    /***
     * Вход пользователя в сервисе по имени и номеру телефона
     * @param userModel - dto пользователя ((имя, телефон)
     * @return  {статус ответа, id id пользователя}
     */
    @SneakyThrows
    public HttpAnswer login(@RequestBody BotUserDto userModel) {

        String url = "http://localhost:" + botConfig.getPortServiceUser() + "/api/v1/admin-service/userInfo/login/owner";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url, new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                if ((response.getBody() != null) && (response.getBody() instanceof BotUserDto)) {
                    httpAnswer.setObject(response.getBody());
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
     * @return  {статус ответа, id id пользователя}
     */
    @SneakyThrows
    public HttpAnswer register(@RequestBody BotUserDto userModel) {

        URL url = new URL("http://localhost:" + botConfig.getPortServiceUser() + "/api/v1/admin-service/userInfo/register/owner");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url.toURI(), new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {

                if ((response.getBody() != null) && (response.getBody() instanceof BotUserDto)) {
                    httpAnswer.setObject(response.getBody());
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
     * @return  {статус ответа, список городо}
     */
    @SneakyThrows
    public HttpAnswer getAllCity() {

        URL url = new URL("http://localhost:" + botConfig.getPortServiceUser() + "/api/v1/admin-service/city");

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
                if ((response.getBody() != null) && (response.getBody() instanceof CityDto[])) {
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
     * Получить список всех моек данного владельца,
     * @param idUser - id владельца
     * @return -  {статус ответа, список моек данного владельца}
     */
    @SneakyThrows
    public HttpAnswer getCarWashListForOwner(Long idUser) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/list-for-owner?idUser={idUser}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CarWashForOwnerDto[]> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<CarWashForOwnerDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarWashForOwnerDto[].class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if ((response.getBody() != null) && (response.getBody() instanceof CarWashForOwnerDto[])) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }


    /***
     * Получить данные об одной мойке
     * @param idCarWash - id мойки
     * @return - {статус ответа, информация о выбранной мойке}
     */
    @SneakyThrows
    public HttpAnswer getCarWashById(Long idCarWash) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/{idCarWash}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CarWashForOwnerDto> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<CarWashForOwnerDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarWashForOwnerDto.class, idCarWash);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if ((response.getBody() != null) && (response.getBody() instanceof CarWashForOwnerDto)) {
                    httpAnswer.setObject(response.getBody());
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }

    /***
     * Добавить новую автомойку для владельца
     * @param userModel - dto автомойки
     * @return {статус ответа, id новой мойки}
     */
    @SneakyThrows
    public HttpAnswer carWashAdd(@RequestBody CarWashForOwnerDto userModel) {

        URL url = new URL("http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/add-by-owner");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<CarWashForOwnerDto> response = restTemplate.postForEntity(url.toURI(), new HttpEntity<>(userModel,httpHeaders), CarWashForOwnerDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {

                if ((response.getBody() != null) && (response.getBody() instanceof CarWashForOwnerDto)) {
                    httpAnswer.setID(response.getBody().getId());
                }
            }

        } catch (Exception ex) {
            log.info("Exception = " + ex);
        }

        return httpAnswer;
    }

    /***
     * Обновить данные автомойки
     * @param userModel - dto автомойки
     * @return {статус ответа, id новой мойки}
     */
    @SneakyThrows
    public HttpAnswer carWashUpdate(@RequestBody CarWashForOwnerDto userModel) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/update-by-owner";

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CarWashForOwnerDto> requestEntity = new HttpEntity<>(userModel, headers);
        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<CarWashForOwnerDto> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CarWashForOwnerDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if ((response.getBody() != null) && (response.getBody() instanceof CarWashForOwnerDto)) {
                    httpAnswer.setObject(response.getBody());
                }

            }

        } catch (Exception ex) {
            log.info("Exception = " + ex);
        }

        return httpAnswer;
    }


    /***
     * Удалить автомойку
     * @param idCarWash - Id мойки
     * @return {статус ответа}
     */
    @SneakyThrows
    public HttpAnswer carWashDel(Long idCarWash) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/{idCarWash}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, idCarWash);

            httpAnswer.setHttpStatus(response.getStatusCode());

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }



    /***
     * Для всех моек данного владельца
     * для каждого дня текущего месяца, начиная с завтра,
     * добавить строки в график работ в соответствии с временем работы и выходными днями
     * @param idUser - id владельца
     * @return - {статус ответа}
     * */
    @SneakyThrows
    public HttpAnswer addTimeTableForCurrentMonth(Long idUser) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/time-add-cur?idUser={idUser}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }

    /***
     * Для всех моек данного владельца
     * для каждого дня следующего месяца
     * добавить строки в график работ в соответствии с временем работы и выходными днями
     * @param idUser - id владельца
     * @return - {статус ответа}
     */
    @SneakyThrows
    public HttpAnswer addTimeTableForNextMonth(Long idUser) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/carWash/time-add-next?idUser={idUser}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class, idUser);

            httpAnswer.setHttpStatus(response.getStatusCode());

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }
        return httpAnswer;
    }


    /***
     * Получить суммарный отчет по услугам моек владельца за период
     * @param idUser - id владельца
     * @param dateBegin - начало периода
     * @param dateEnd - конец периода
     * @return - {статус ответа, данные отчета}
     */

    @SneakyThrows
    public HttpAnswer getSumReportFromAllTimeTableByOwner(Long idUser, String dateBegin, String dateEnd) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/timeTableSumReport/sum-report?idUser={idUser}&dateBegin={dateBegin}&dateEnd={dateEnd}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableSumReportDto> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableSumReportDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableSumReportDto[].class, idUser, dateBegin, dateEnd);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if ((response.getBody() != null) && (response.getBody() instanceof TimeTableSumReportDto[])) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }

    /***
     * Получить список заказов для выбранной мойки за выбранную дату
     * @param idCarWash - id мойки
     * @param dateOrder - дата
     * @return - {статус ответа, данные отчета}
     */

    @SneakyThrows
    public HttpAnswer getTimeTableByIdCarWashByDate(Long idCarWash, String dateOrder) {

        String url = "http://localhost:" + botConfig.getPortServiceCarWashForOwner() + "/api/v1/CarWash-service/timeTable/order-by-date?idCarWash={idCarWash}&dateOrder={dateOrder}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TimeTableDto> requestEntity = new HttpEntity<>(null, headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TimeTableDto[].class, idCarWash, dateOrder);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {

                if ((response.getBody() != null) && (response.getBody() instanceof TimeTableDto[])) {
                    httpAnswer.setObjectList(List.of(response.getBody()));
                }

            }

        } catch (Exception ex) {
            log.error("Exception " + ex);

        }

        return httpAnswer;
    }


}
