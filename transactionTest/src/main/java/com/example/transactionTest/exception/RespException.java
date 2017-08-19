package com.example.transactionTest.exception;

import java.io.Serializable;

/**
 * Created by martin on 2017/8/17.
 */
public class RespException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6923842158284700407L;
    private String retCode;
    private String resMsg;

    public RespException() {

    }

    public RespException(String retCode, String resMsg) {
        this.retCode = retCode;
        this.resMsg = resMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
