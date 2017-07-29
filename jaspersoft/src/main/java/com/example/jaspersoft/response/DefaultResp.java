package com.example.jaspersoft.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * Created by martin on 2017/7/29.
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class DefaultResp implements Serializable {

    private static final long serialVersionUID = -8085776008895035968L;
    private String code;
    private String message;
    private Object data;

    public DefaultResp() {
    }

    public DefaultResp(String code) {
        this.code = code;
    }

    public DefaultResp(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public DefaultResp(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
