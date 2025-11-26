package com.jjubul.authserver.exception;

public class UserDuplicatedException extends BusinessException {
    public UserDuplicatedException(String message) {
        super(ErrorCode.USER_DUPLICATED_EXCEPTION);
    }
}
