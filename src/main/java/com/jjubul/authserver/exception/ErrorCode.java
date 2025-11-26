package com.jjubul.authserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**토큰 예외**/
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "리프레시 토큰을 찾지 못하였습니다."),

    /**사용자 중복 예외**/
    USER_DUPLICATED_EXCEPTION(HttpStatus.CONFLICT, "중복된 사용자가 회원가입을 시도하였습니다.");

    private final HttpStatus status;
    private final String message;
}
