package com.jjubul.authserver.exception;

public class RefreshTokenExpiredException extends BusinessException {
    public RefreshTokenExpiredException(String message) {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
