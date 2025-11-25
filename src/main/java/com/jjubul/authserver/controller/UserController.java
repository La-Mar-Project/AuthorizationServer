package com.jjubul.authserver.controller;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.dto.NewUserDto;
import com.jjubul.authserver.dto.RefreshTokenDto;
import com.jjubul.authserver.dto.response.ApiResponse;
import com.jjubul.authserver.service.OAuth2UserService;
import com.jjubul.authserver.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuth2UserService oAuth2UserService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody NewUserDto dto) {

        log.info("jwt={}", dto.getJwt());
        log.info("jwt={}", dto.getUsername());

        OAuth2User user = oAuth2UserService.newUser(dto.getJwt(), dto.getUsername(), dto.getNickname(), dto.getPhone());
        String myAccessToken = tokenService.buildMyAccessToken(user);
        RefreshTokenDto refreshTokenDto = tokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenDto.getCookie().toString())
                .body(ApiResponse.success("회원가입을 성공하였습니다."));
    }
}
