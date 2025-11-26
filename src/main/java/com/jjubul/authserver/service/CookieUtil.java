package com.jjubul.authserver.service;

import org.springframework.http.ResponseCookie;

// 배포 수정
public class CookieUtil {

    public static ResponseCookie buildCookie(String cookieName, Object cookieValue) {
        return ResponseCookie.from(cookieName, cookieValue.toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60L * 60 * 24 * 14)
                .build();
    }
}
