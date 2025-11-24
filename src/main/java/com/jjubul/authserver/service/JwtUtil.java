package com.jjubul.authserver.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JWKSource<SecurityContext> jwkSource;

    public String verifyToken(SignedJWT signedJWT, JWSVerifier verifier) {
        try {
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid Signature");
            }

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new RuntimeException("Expired Token");
            }

            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyOpToken(String issuer, String aud, SignedJWT jwt) throws ParseException {
        if (!issuer.equals(jwt.getJWTClaimsSet().getIssuer())) {
            throw new RuntimeException("Invalid Issuer");
        }

        if (!aud.equals(jwt.getJWTClaimsSet().getAudience().getFirst())) {
            throw new RuntimeException("Invalid Client Id");
        }
    }

    public String buildMyToken(RSAKey rsaKey, String sub, String provider, String grade) {
        try {
            JWTClaimsSet claims = buildBody(sub, provider, grade);

            JWSHeader header = buildHeader(rsaKey);

            SignedJWT jwt = buildSign(rsaKey, header, claims);

            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT buildSign(RSAKey rsaKey, JWSHeader header, JWTClaimsSet claims) throws JOSEException {
        RSASSASigner signer = new RSASSASigner(rsaKey);
        SignedJWT jwt = new SignedJWT(header, claims);
        jwt.sign(signer);
        return jwt;
    }

    private JWSHeader buildHeader(RSAKey rsaKey) {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.getKeyID())
                .build();
    }

    private JWTClaimsSet buildBody(String sub, String provider, String grade) {
        return new JWTClaimsSet.Builder()
                .subject(sub)
                .issuer("jjubul-auth-server")
                .audience("jjubul-api-server")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 10 * 60 * 1000))
                .claim("provider", provider)
                .claim("grade", grade)
                .build();
    }

    public RSAKey getMyRsaKey(String kid) throws KeySourceException {
        JWKSelector selector = new JWKSelector(
                new JWKMatcher.Builder()
                        .keyID(kid)
                        .build()
        );

        JWK jwk = jwkSource.get(selector, null).getFirst();
        return jwk.toRSAKey();
    }

    public RSAKey getOpRsaKey(String keyUrl, String kid) throws IOException, ParseException {
        JWKSet jwkSet = JWKSet.load(new URL(keyUrl));
        JWK jwk = jwkSet.getKeyByKeyId(kid);

        return jwk.toRSAKey();
    }
}
