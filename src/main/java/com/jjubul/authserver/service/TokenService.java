package com.jjubul.authserver.service;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.authorization.RefreshToken;
import com.jjubul.authserver.dto.RefreshTokenDto;
import com.jjubul.authserver.repository.RefreshTokenRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWKSource<SecurityContext> jwkSource;
    private final JwtUtil jwtUtil;

    public String buildMyAccessToken(OAuth2User user) {
        try {
            JWK jwk = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null).getFirst();
            RSAKey rsaKey = jwk.toRSAKey();

            return jwtUtil.buildMyToken(rsaKey, user.getSub(), user.getProvider().toString(), user.getGrade().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RefreshTokenDto createRefreshToken(Long userId) {

        String token = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(60L * 60 * 24 * 14);

        RefreshToken refreshToken = RefreshToken.create(userId, token, expiresAt);
        ResponseCookie cookie = CookieUtil.buildCookie("refresh_token", refreshToken.getValue());

        refreshTokenRepository.save(refreshToken);

        return RefreshTokenDto.builder()
                .refreshToken(refreshToken.getValue())
                .cookie(cookie)
                .build();
    }

    public Long verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByValue(token)
                .orElseThrow(EntityNotFoundException::new);

        if (Instant.now().isAfter(refreshToken.getExpiresAt())) {
            throw new RuntimeException("Expired Token");
        }

        return refreshToken.getId();
    }

    public RefreshTokenDto deleteRefreshToken(String token) {

        refreshTokenRepository.deleteByValue(token);

        ResponseCookie cookie = CookieUtil.buildCookie("refresh_token", "");

        return RefreshTokenDto.builder()
                .refreshToken("")
                .cookie(cookie)
                .build();
    }

}
