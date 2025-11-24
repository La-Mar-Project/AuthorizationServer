package com.jjubul.authserver.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@Builder
public class RefreshTokenDto {

    ResponseCookie cookie;
    String refreshToken;

    public RefreshTokenDto(ResponseCookie cookie, String refreshToken) {
        this.cookie = cookie;
        this.refreshToken = refreshToken;
    }
}
