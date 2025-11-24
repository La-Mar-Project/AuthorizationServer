package com.jjubul.authserver.exception;

import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.dto.OAuth2InfoDto;
import com.jjubul.authserver.dto.response.ApiResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final JWKSource<SecurityContext> jwkSource;

    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException(UserNotFoundException e, HttpServletResponse response) throws IOException, JOSEException {

        JWK jwk = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null).getFirst();

        RSAKey rsaKey = jwk.toRSAKey();

        RSASSASigner signer = new RSASSASigner(rsaKey);

        JWTClaimsSet claims = buildJwt(e.getProvider(), e.getSub());

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.getKeyID())
                .build();

        SignedJWT jwt = new SignedJWT(header, claims);

        jwt.sign(signer);

        String redirectUrl = buildRedirectUrl(jwt);

        response.sendRedirect(redirectUrl);
    }

    private static String buildRedirectUrl(SignedJWT jwt) {
        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:5200/signup")
                .queryParam("jwt", jwt.serialize())
                .build()
                .toUriString();
        return redirectUrl;
    }

    private static JWTClaimsSet buildJwt(Provider provider, String sub) {
        return new JWTClaimsSet.Builder()
                .subject(sub)
                .issuer("jjubul-auth-server")
                .audience("jjubul-auth-server")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 10 * 60 * 1000))
                .claim("provider", provider)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
