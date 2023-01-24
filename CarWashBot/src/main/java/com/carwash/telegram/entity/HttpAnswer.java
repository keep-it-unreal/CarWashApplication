package com.carwash.telegram.entity;

import org.springframework.http.HttpStatus;

import java.util.List;

public class HttpAnswer {
    private Long ID;
    private HttpStatus httpStatus;
    private String status;
    private boolean success;

    private List<?> objectList;

    public HttpAnswer() {
        success = false;
        status  = "При выполнении запроса произошла неизвестная ошибка";
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setObjectList(List<?> objectList) {
        this.objectList = objectList;
    }

    public List<?> getObjectList() {
        return objectList;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;

        if (httpStatus == HttpStatus.OK ||
                httpStatus == HttpStatus.CREATED ||
                httpStatus == HttpStatus.NO_CONTENT) {
            status = "Операция завершена успешно";
            success = true;

        } else {
            success = false;

            if (httpStatus == HttpStatus.NOT_FOUND) {
                status = "По указанным условиям ничего не найдено";
            } else if (httpStatus == HttpStatus.GATEWAY_TIMEOUT) {
                status = "Превышено время ожидания";
            } else {
                status = "При выполнении запроса произошла неизвестная ошибка";
            }
        }
    }

    public Long getID() {
        return ID;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
