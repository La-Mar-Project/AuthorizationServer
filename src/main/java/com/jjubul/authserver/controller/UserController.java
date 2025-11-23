package com.jjubul.authserver.controller;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.dto.NewUserDto;
import com.jjubul.authserver.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuth2UserService oAuth2UserService;

    @PostMapping("/users")
    public OAuth2User newUser(NewUserDto dto) {
        return oAuth2UserService.newUser(dto.getProvider(), dto.getSub(), dto.getUsername(), dto.getNickname(), dto.getPhone());
    }
}
