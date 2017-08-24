package com.example.jaspersoft.exception;


public class JaspException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JaspException() {
        super();
    }

    public JaspException(String message) {
        super(message);
    }

}
