package com.jjubul.authserver.service;

import com.jjubul.authserver.authorization.User;
import com.jjubul.authserver.exception.UserNotFoundException;
import com.jjubul.authserver.authorization.Grade;
import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.repository.OAuth2UserRepository;
import com.jjubul.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService {

    private final OAuth2UserRepository oAuth2UserRepository;
    private final UserRepository userRepository;

    public OAuth2User getUser(String sub, Provider provider) {
        return oAuth2UserRepository.findOAuth2UserByProviderAndSub(provider, sub)
                .orElseThrow(() -> new UserNotFoundException(provider, sub));
    }

    public OAuth2User getUser(Long id) {
        return oAuth2UserRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public OAuth2User newUser(Provider provider, String sub, String username, String nickname, String phone) {
        if (oAuth2UserRepository.existsByProviderAndSub(provider, sub)) {
            throw new IllegalArgumentException("user already exists");
        }

        userRepository.save(User.create(username, nickname, Grade.BASIC , phone, sub, provider));
        return oAuth2UserRepository.save(OAuth2User.create(provider, sub, username));
    }
}
