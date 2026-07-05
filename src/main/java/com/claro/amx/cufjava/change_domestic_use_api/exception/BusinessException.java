package com.claro.amx.cufjava.change_domestic_use_api.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

    private final String code;
    private final String message;
    private final String source;

    public BusinessException(String code, String message, String source) {
        super(message);
        this.code = code;
        this.message = message;
        this.source = source;
    }

    public BusinessException(String code, String message, String source, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.source = source;
    }

    @Override
    public String toString() {
        return "BusinessException{code='%s', message='%s', source='%s'}".formatted(code, message, source);
    }
}
