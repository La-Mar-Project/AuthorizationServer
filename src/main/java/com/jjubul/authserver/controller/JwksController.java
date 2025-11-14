package com.jjubul.authserver.controller;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JWKSource<SecurityContext> jwkSource;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() throws Exception {

        List<JWK> jwks = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null);
        JWKSet jwkSet = new JWKSet(jwks);

        return jwkSet.toJSONObject();
    }
}
