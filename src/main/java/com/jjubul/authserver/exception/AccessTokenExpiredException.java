package com.jjubul.authserver.exception;

public class AccessTokenExpiredException extends BusinessException {
    public AccessTokenExpiredException(String message) {
        super(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
