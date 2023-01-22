package com.carwash.telegram.core;

import com.carwash.telegram.entity.HttpAnswer;
import com.carwash.telegram.entity.dto.*;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {

    private final RestTemplate restTemplate;

    //carWash
    private final static String TAG_ID = "id";
    private final static String TAG_ADDRESS = "address";
    private final static String TAG_PRICE = "price";
    private final static String TAG_LATIT = "latitude";
    private final static String TAG_LONG = "longitude";

    //userInfo
    private final static String TAG_USER_DTO = "UserInfoDto";
    private final static String TAG_USER_ID = "id";

    //city
    private final static String TAG_CITY_ID = "id";
    private final static String TAG_CITY_NAME = "name";

    /**
     * Вход пользователя в сервисе по имени и номеру телефона
     * @param userModel - dto пользователя ((имя, телефон)
     * @return id пользователя, если пользователь зарегистрирован в сервисе, 0 - если нет
     */
    @SneakyThrows
    public HttpAnswer login(@RequestBody BotUserDto userModel) {

        //String url = "http://localhost:8083/api/v1/admin-service/userInfo/login/user";
        String url = "http://localhost:8080/api/v1/admin-service/userInfo/login/user";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url, new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                httpAnswer.setID(response.getBody().getId());
            }

        } catch (Exception ex) {
            log.info("Method BotController.login(). Exception = " + ex);
        }

        return httpAnswer;
    }

    /**
     * Регистрация пользователя в сервисе (имя, телефон, idCity)
     * @param userModel - dto пользователя
     * @return id пользователя, если пользователь зарегистрирован в сервисе, 0 - если нет
     */
    @SneakyThrows
    public HttpAnswer register(@RequestBody BotUserDto userModel) {

        URL url = new URL("http://localhost:8080/api/v1/admin-service/userInfo/register/user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            ResponseEntity<BotUserDto> response = restTemplate.postForEntity(url.toURI(), new HttpEntity<>(userModel,httpHeaders), BotUserDto.class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {

                httpAnswer.setID(response.getBody().getId());
            }

        } catch (Exception ex) {
            log.info("Method BotController.register(). Exception = " + ex);
        }

        return httpAnswer;
    }

    /**
     * Получение списка всх городов
     *
     * @return список городо
     */
    @SneakyThrows
    public HttpAnswer getAllCity() {

        URL url = new URL("http://localhost:8080/api/v1/admin-service/city");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        CityDto[] allCityDtos = {};

        HttpAnswer httpAnswer = new HttpAnswer();
        try {
            RestTemplate restTemplate = new RestTemplate();

            //CityListDto responce = restTemplate.getForObject(url.toURI(),CityListDto.class);

            ResponseEntity<CityDto[]> response =
                    restTemplate.getForEntity(
                            url.toURI(),
                            CityDto[].class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {
                allCityDtos = response.getBody();
                httpAnswer.setObjectList(List.of(allCityDtos));
            }

        } catch (Exception ex) {
            log.error("Method BotController.getAllCity(). Exception " + ex);
        }

        return httpAnswer;
    }

    /**
     * Получить список всех моек,
     * находящихсы в городе, в котором зарегистрирован пользователь
     * у которых есть незанятое время в выбранную пользователем дату
     * @param nameUser - полное имя пользователя
     * @param date - выбранная дата
     * @return - список свободных моек в городе пользователя
     */
    @SneakyThrows
    public List<CarWashDto> getAllCarWash(String nameUser, String date) {

        URL url = new URL("http://localhost:8080/api/v1/CarWash-service/carWash/all-for-user-on-date?user={nameUser}&date={date}");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        CarWashDto[] carWashDtos = {};

        try {
            RestTemplate restTemplate = new RestTemplate();

            /*
            Map<String,String> map = new HashMap<>();
            map.put("nameUser",nameUser);
            map.put("date",date);
             */

            //CarWashListDto responce = restTemplate.getForObject(url.toString(),CarWashListDto.class,map);

            carWashDtos = restTemplate.getForObject(url.toString(),CarWashDto[].class,
                    nameUser, date);

        } catch (Exception ex) {
            log.error("Method getAllCity. Exception " + ex);
        }

        return List.of(carWashDtos);

    }

    /**
     * Получить список всех свободных позиций времени для записи пользователя на автомойку
     * для выбранной пользователем даты, мойки
     * @param date - выбранная дата
     * @param idCarWash - id выбранной мойки
     * @return - список свободных позиций времени
     */
    @SneakyThrows
    public List<TimeTableDto> getAllTime(String date, Long idCarWash ) {

        URL url = new URL("http://localhost:8080/api/v1/CarWash-service/timeTable/?date={date}&idCarWash={idCarWash}");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        TimeTableDto[] timeTableDtos = {};

        try {
            RestTemplate restTemplate = new RestTemplate();

            timeTableDtos = restTemplate.getForObject(url.toString(),TimeTableDto[].class,
                    date, idCarWash);

        } catch (Exception ex) {
            log.error("Method getAllCity. Exception " + ex);
        }

        return List.of(timeTableDtos);

    }

    /**
     * Оформление заказа пользователдя на мойку машины
     * @param timeTableDto - данные заказа
     * @return true - заказ оформлен успешно
     */
    @SneakyThrows
    public HttpAnswer orderOn(@RequestBody TimeTableDto timeTableDto) {
        String url = "http://localhost:8080/api/v1/CarWash-service/timeTable/order-on";

        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        //headers.set("X-TP-DeviceID", Global.deviceID);
        //Map<String, String> param = new HashMap<String, String>();
        //param.put("id","10")

        HttpEntity<TimeTableDto> requestEntity = new HttpEntity<TimeTableDto>(timeTableDto,headers);
        HttpAnswer httpAnswer = new HttpAnswer();

        try {
            //ResponseEntity<TimeTableDto> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, TimeTableDto[].class, param);
            ResponseEntity<TimeTableDto[]> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, TimeTableDto[].class);

            httpAnswer.setHttpStatus(response.getStatusCode());
            if (httpAnswer.isSuccess()) {
                httpAnswer.setObjectList(List.of(response.getBody()));
            }

        } catch (Exception ex) {
            log.error("Method BotController.orderOn(). Exception " + ex);

        }

        return httpAnswer;
    }

    /*
    @SneakyThrows
    public List<CarWashDto> getNeaCarWash(String lattid, String longt) {
        List<CarWashDto> neaCarWashModelList = new ArrayList<>();

        String urlParameters  = String.format("?latitude=%s&longitude=%s&date=%s", lattid, longt, currDate);
        URL url = new URL("http://localhost:8080/car-wash/nearest-free-car-washes-by-date" + urlParameters);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        JSONArray jsonArray = new JSONArray(response.toString());
        in.close();

        for (Object jo : jsonArray) {
            JSONObject jsonObject = (JSONObject) jo;
            CarWashDto neaCarWashModel = new CarWashDto();
            neaCarWashModel.setId(jsonObject.getInt(TAG_ID));
            neaCarWashModel.setAddress(jsonObject.getString(TAG_ADDRESS));

            if (!jsonObject.isNull(TAG_PRICE)) {
                neaCarWashModel.setPrice(jsonObject.getDouble(TAG_PRICE));
            } else {
                neaCarWashModel.setPrice(0);
            }

            neaCarWashModelList.add(neaCarWashModel);
        }

        return neaCarWashModelList;
    }

     */

    /*
    @SneakyThrows
    public String createOrder(Long carWashId) {
        URL url = new URL("http://localhost:8080/order/create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JsonObject jsonObject = new com.google.gson.JsonObject();
        //jsonObject.addProperty("userId", userSignUpId);
        jsonObject.addProperty("status", "Новый");
        jsonObject.addProperty("carWashId", carWashId);
        jsonObject.addProperty("date", currDate);

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
        wr.close();

        InputStream in = new BufferedInputStream(connection.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        JSONObject jsonResultObject = new JSONObject(result);

        in.close();
        connection.disconnect();

        return BotText.SELECT_ALL;
    }

     */


}
