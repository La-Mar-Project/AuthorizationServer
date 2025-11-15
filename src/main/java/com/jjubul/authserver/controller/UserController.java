package com.jjubul.authserver.controller;

import com.jjubul.authserver.authentication.LocalUser;
import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.dto.NewLocalUserDto;
import com.jjubul.authserver.dto.NewOAuth2UserDto;
import com.jjubul.authserver.service.LocalUserService;
import com.jjubul.authserver.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuth2UserService oAuth2UserService;
    private final LocalUserService localUserService;

    @PostMapping("/user/oauth2")
    public OAuth2User newUser(NewOAuth2UserDto dto) {
        return oAuth2UserService.newUser(dto.getProvider(), dto.getSub(), dto.getEmail(), dto.getName());
    }

    @PostMapping("/user/local")
    public OAuth2User newUser(NewLocalUserDto dto) {
        LocalUser localUser = localUserService.newUser(dto.getAccount(), dto.getPassword());
        return oAuth2UserService.newUser(Provider.LOCAL, localUser.getId().toString(), dto.getEmail(), dto.getName());
    }

}
