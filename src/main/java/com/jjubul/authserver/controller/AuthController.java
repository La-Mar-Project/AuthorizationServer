package com.jjubul.authserver.controller;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.dto.RefreshTokenDto;
import com.jjubul.authserver.dto.response.ApiResponse;
import com.jjubul.authserver.service.JwtUtil;
import com.jjubul.authserver.service.LoginService;
import com.jjubul.authserver.service.TokenService;
import com.jjubul.authserver.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final TokenService tokenService;
    private final OAuth2UserService userService;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(@CookieValue("refresh_token") String refreshToken) {

        Long userId = tokenService.verifyRefreshToken(refreshToken);

        OAuth2User user = userService.getUser(userId);

        tokenService.deleteRefreshToken(refreshToken);

        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());

        String accessToken = tokenService.buildMyAccessToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("리프레시 토큰을 사용하여 액세스 토큰을 발급하였습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@CookieValue("refresh_token") String refreshToken) {

        RefreshTokenDto refreshTokenDto = tokenService.deleteRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("로그아웃에 성공하였습니다."));
    }

    @GetMapping("/{provider}/start")
    public ResponseEntity<Void> authStart(@PathVariable String provider) {

        String url = loginService.start(provider);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/{provider}/callback")
    public ResponseEntity<Void> callback(@RequestParam String code, @PathVariable String provider) {

        String idToken = loginService.callBack(code, provider);

        OAuth2User user = userService.getUserByToken(idToken, provider);

        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());

        String redirectUrl = "http://localhost:5200/home";

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }
}
