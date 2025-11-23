package com.jjubul.authserver.dummy;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@RequiredArgsConstructor
public class DummyUser implements CommandLineRunner {

    private final OAuth2UserService userService;

    @Override
    public void run(String... args) throws Exception {
        OAuth2User user = OAuth2User.create(Provider.KAKAO, "4543976062", "원종윤");
        userService.newUser(Provider.KAKAO, "4543976062", "원종윤", "종윤", "010-6326-1851");
    }
}
