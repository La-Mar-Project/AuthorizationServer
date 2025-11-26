package com.jjubul.authserver.exception;

public class DuplicatedUserException extends BusinessException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}
