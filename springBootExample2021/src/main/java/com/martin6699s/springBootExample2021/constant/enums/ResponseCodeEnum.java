package com.martin6699s.springBootExample2021.constant.enums;

public enum ResponseCodeEnum {

    SUCCESS(0, "success");


    private Integer code;
    private String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
