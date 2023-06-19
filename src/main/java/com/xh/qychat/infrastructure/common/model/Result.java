package com.xh.qychat.infrastructure.common.model;

import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> of(T data, Integer code, String msg) {

        return new Result<>(data, code, msg);
    }

    public static <T> Result<T> succeed(String msg) {

        return of(null, ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getNote());
    }

    public static <T> Result<T> succeed(T model, String msg) {

        return of(model, ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> operateSucceed() {

        return of(null, ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getNote());
    }

    public static <T> Result<T> failed(String msg) {

        return of(null, ResponseEnum.INTERNAL_SERVER_FAIL.getCode(), msg);
    }

    public static <T> Result<T> failed(Integer code, String msg) {

        return of(null, code, msg);
    }

    public static <T> Result<T> failed(T model, Integer code, String msg) {

        return of(model, code, msg);
    }

    public static <T> Result<T> failed(ResponseEnum response) {

        return of(null, response.getCode(), response.getNote());
    }

    public static <T> Result<T> failed(T model, ResponseEnum response) {

        return of(model, response.getCode(), response.getNote());
    }
}
