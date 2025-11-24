package com.jjubul.authserver.service;

import com.jjubul.authserver.authorization.User;
import com.jjubul.authserver.dto.ValidatedMyTokenDto;
import com.jjubul.authserver.exception.KeyException;
import com.jjubul.authserver.exception.UserNotFoundException;
import com.jjubul.authserver.authorization.Grade;
import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.repository.OAuth2UserRepository;
import com.jjubul.authserver.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService {

    private final OAuth2UserRepository oAuth2UserRepository;
    private final UserRepository userRepository;
    private final TokenValidator tokenValidator;

    public OAuth2User getUser(String sub, Provider provider) {
        return oAuth2UserRepository.findOAuth2UserByProviderAndSub(provider, sub)
                .orElseThrow(() -> new UserNotFoundException(provider, sub));
    }

    public OAuth2User getUser(Long id) {
        return oAuth2UserRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public OAuth2User getUserByToken(String token, String provider) {

        String sub = tokenValidator.validateOpToken(token, provider);

        return oAuth2UserRepository.findOAuth2UserByProviderAndSub(Provider.from(provider), sub)
                .orElseThrow(() -> new UserNotFoundException(Provider.from(provider), sub));
    }

    public OAuth2User newUser(String jwt, String username, String nickname, String phone) {

        ValidatedMyTokenDto dto = tokenValidator.validateMyToken(jwt);
        Provider provider = dto.getProvider();
        String sub = dto.getSub();

        if (oAuth2UserRepository.existsByProviderAndSub(provider, sub)) {
                throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        userRepository.save(User.create(username, nickname, Grade.BASIC, phone, sub, provider));
        return oAuth2UserRepository.save(OAuth2User.create(provider, sub, username));
    }
}
