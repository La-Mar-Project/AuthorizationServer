package com.jjubul.authserver.exception;

public class RefreshTokenNotFoundException extends BusinessException {
    public RefreshTokenNotFoundException(ErrorCode code) {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
