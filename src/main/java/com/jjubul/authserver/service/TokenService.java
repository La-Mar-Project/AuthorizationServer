package com.jjubul.authserver.service;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.authorization.RefreshToken;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWKSource<SecurityContext> jwkSource;

    public String toLocalToken(OAuth2User user) {
        try {
            JWK jwk = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null).getFirst();
            RSAKey rsaKey = jwk.toRSAKey();
            RSASSASigner signer = new RSASSASigner(rsaKey);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getSub())
                    .issuer("jjubul-auth-server")
                    .audience("jjubul-api-server")
                    .issueTime(new Date())
                    .expirationTime(new Date(new Date().getTime() + 10 * 60 * 1000))
                    .claim("provider", user.getProvider().toString())
                    .claim("grade", user.getGrade().toString())
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .build();

            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(signer);

            return jwt.serialize();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String createRefreshToken(Long userId) {

        String token = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(60L * 60 * 24 * 14);

        RefreshToken refreshToken = RefreshToken.create(userId, token, expiresAt);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        return saved.getValue();
    }

    public Long verifyRefreshToken(String token) {
        RefreshToken rt = refreshTokenRepository.findByValue(token);

        if (Instant.now().isAfter(rt.getExpiresAt())) {
            throw new RuntimeException("Expired Token");
        }

        return rt.getUserId();
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByValue(token);
    }

    public String verifyToken(String idToken, String provider) {
        try {

            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);

            JWKSet jwkSet = JWKSet.load(new URL(clientRegistration.getProviderDetails().getJwkSetUri()));

            SignedJWT signedJWT = SignedJWT.parse(idToken);

            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());

            RSASSAVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());
            boolean isValid = signedJWT.verify(verifier);
            if (!isValid) {
                throw new RuntimeException("Invalid Signature");
            }

            if (!clientRegistration.getProviderDetails().getIssuerUri().equals(signedJWT.getJWTClaimsSet().getIssuer())) {
                throw new RuntimeException("Invalid Issuer");
            }

            if (!clientRegistration.getClientId().equals(signedJWT.getJWTClaimsSet().getAudience().getFirst())) {
                throw new RuntimeException("Invalid Client Id");
            }

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new RuntimeException("Expired Token");
            }

            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
