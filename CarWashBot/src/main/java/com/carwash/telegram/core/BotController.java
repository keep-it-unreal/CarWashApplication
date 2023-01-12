package com.carwash.telegram.core;

import com.carwash.telegram.commands.BotText;
import com.carwash.telegram.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    private String currDate;

    private Long userSignUpId;

    public Long getUserSignUpId() {
        return userSignUpId;
    }

    /*
    public BotController() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.currDate = dateFormat.format(date);
    }
     */

    /*
    Проверить пользователя по имени и номеру телефона
    */
    @SneakyThrows
    public String login(@RequestBody BotUserModel userModel) {



        //отправляем запрос
        //        URL url = new URL("http://localhost:8083/user-service/api/createCarWashUser");
        URL url = new URL("http://localhost:8080/api/v1/admin-service/userInfo/login");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        BotUserModel userModel1 = new BotUserModel();

        //userModel1.setName(userModel.getName());
        //userModel1.setPhone(userModel.getPhone());
        userModel1.setName("user01");
        userModel1.setPhone("8-910-001-02-01");


        String result="Вы успешно зарегистрировались";
        try {
            ResponseEntity<BotUserModel> response = restTemplate.postForEntity(url.toURI(), new HttpEntity<>(userModel1,httpHeaders),BotUserModel.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                userSignUpId = response.getBody().getId();
            }

        } catch (Exception ex) {
            result ="Учетная запись не найдена";
        }


        /*
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        var jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("name", userModel.getName());
        jsonObject.addProperty("phone", userModel.getPhone());
        //jsonObject.addProperty("name", "user01");
        //jsonObject.addProperty("phone", "8-910-001-02-01");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
        wr.close();

        //получаем ответ
        String result="Вы успешно зарегистрировались";
        userSignUpId = 0L;
        try {
            JSONObject inputJSONobj =  new JSONObject(new JSONTokener(new InputStreamReader(connection.getInputStream())));
            userSignUpId = inputJSONobj.getLong(TAG_USER_ID);
        } catch (Exception ex) {
            result ="Учетная запись не найдена";
        } finally {
            connection.disconnect();
        }
*/
        return result;
    }

    @SneakyThrows
    public String signUp(@RequestBody BotUserModel userModel) {
//        URL url = new URL("http://localhost:8083/user-service/api/createCarWashUser");
        URL url = new URL("http://localhost:8080/api/v1/admin-service/userInfo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        var jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("nickName", userModel.getName());
        jsonObject.addProperty("phone", userModel.getPhone());
        jsonObject.addProperty("fromTlg", true);

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JSONObject inputJSONobj =  new JSONObject(new JSONTokener(new InputStreamReader(connection.getInputStream())));

        JSONObject jsonDto = inputJSONobj.getJSONObject(TAG_USER_DTO);
        userSignUpId = jsonDto.getLong(TAG_USER_ID);

        in.close();
        connection.disconnect();

        return "Вы успешно зарегистрировались";
    }

    @SneakyThrows
    public List<CityModel> getAllCity() {
        List<CityModel> allCityModelList = new ArrayList<>();

        URL url = new URL("http://localhost:8080/api/v1/admin-service/city");
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
            CityModel allCityModel = new CityModel();
            allCityModel.setId(jsonObject.getLong(TAG_CITY_ID));
            allCityModel.setName(jsonObject.getString(TAG_CITY_NAME));

            allCityModelList.add(allCityModel);
        }

        return allCityModelList;
    }

    @SneakyThrows
    public List<AllCarWashModel> getAllCarWash() {
        List<AllCarWashModel> allCarWashModelList = new ArrayList<>();

        URL url = new URL("http://localhost:8080/car-wash/all");
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
            AllCarWashModel allCarWashModel = new AllCarWashModel();
            allCarWashModel.setId(jsonObject.getInt(TAG_ID));
            allCarWashModel.setAddress(jsonObject.getString(TAG_ADDRESS));
            allCarWashModel.setLatitude(String.valueOf(jsonObject.getString(TAG_LATIT)));
            allCarWashModel.setLongitude(String.valueOf(jsonObject.getString(TAG_LONG)));

            allCarWashModelList.add(allCarWashModel);
        }


        return allCarWashModelList;
    }

    @SneakyThrows
    public List<NeaCarWashModel> getNeaCarWash(String lattid, String longt) {
        List<NeaCarWashModel> neaCarWashModelList = new ArrayList<>();

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
            NeaCarWashModel neaCarWashModel = new NeaCarWashModel();
            neaCarWashModel.setId(jsonObject.getInt(TAG_ID));
            neaCarWashModel.setAddress(jsonObject.getString(TAG_ADDRESS));

            if (!jsonObject.isNull(TAG_PRICE)) {
                neaCarWashModel.setPrice(String.valueOf(jsonObject.getInt(TAG_PRICE)));
            } else {
                neaCarWashModel.setPrice("0");
            }

            neaCarWashModelList.add(neaCarWashModel);
        }


        return neaCarWashModelList;
    }

    @SneakyThrows
    public String createOrder(Long carWashId) {
        URL url = new URL("http://localhost:8080/order/create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        var jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("userId", userSignUpId);
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

        return BotText.SIGN_ORDER_TEXT;
    }


}
