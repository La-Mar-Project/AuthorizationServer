package com.jjubul.authserver.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode code;

    public BusinessException(ErrorCode code){
        super(code.getMessage());
        this.code = code;
    }

    public BusinessException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

}