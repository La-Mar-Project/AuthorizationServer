package com.jjubul.authserver.exception;

import com.jjubul.authserver.dto.OAuth2InfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<OAuth2InfoDto> handleUserNotFoundException(UserNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OAuth2InfoDto(e.getProvider(), e.getSub()));
    }
}
