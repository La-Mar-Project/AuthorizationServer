package com.jjubul.authserver.controller;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
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

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final LoginService loginService;
    private final TokenService tokenService;
    private final OAuth2UserService userService;

    @PostMapping("/token/refresh")
    public ResponseEntity refreshToken(@CookieValue("refresh_token") String refreshToken) {

        Long userId = tokenService.verifyRefreshToken(refreshToken);

        tokenService.deleteRefreshToken(refreshToken);

        OAuth2User user = userService.getUser(userId);

        String newAt = tokenService.toLocalToken(user);
        String newRt = tokenService.createRefreshToken(user.getId());

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRt)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60L * 60 * 24 * 14)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("accessToken", newAt));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@CookieValue("refresh_token") String refreshToken) {

        tokenService.deleteRefreshToken(refreshToken);

        ResponseCookie expired = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60L * 60 * 24 * 14)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expired.toString())
                .body("logout success");
    }

    @GetMapping("/{provider}/start")
    public ResponseEntity<Void> authStart(@PathVariable String provider) {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);

        String url =
                clientRegistration.getProviderDetails().getAuthorizationUri() +
                        "?response_type=code" +
                        "&client_id=" + clientRegistration.getClientId() +
                        "&redirect_uri=" + clientRegistration.getRedirectUri() +
                        "&scope=openid";

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }


    @GetMapping("/{provider}/callback")
    public ResponseEntity<String> callback(@RequestParam String code, @PathVariable String provider) {

        String idToken = loginService.callBack(code, provider);

        String sub = tokenService.verifyToken(idToken, provider);

        OAuth2User user = userService.getUser(sub, Provider.from(provider));

        String localToken = tokenService.toLocalToken(user);

        String refreshToken = tokenService.createRefreshToken(user.getId());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60L * 60 * 24 * 14)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(localToken);
    }
}
