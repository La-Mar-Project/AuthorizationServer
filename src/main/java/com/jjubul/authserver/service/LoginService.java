package com.jjubul.authserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final WebClient.Builder webClientBuilder;

    public String callBack(String code, String provider) {

        ClientRegistration clientRegistration =
                clientRegistrationRepository.findByRegistrationId(provider);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientRegistration.getClientId());
        params.add("client_secret", clientRegistration.getClientSecret());
        params.add("redirect_uri", clientRegistration.getRedirectUri());
        params.add("code", code);
//
//        Map<String, Object> response = webClientBuilder.build()
//                .post()
//                .uri(clientRegistration.getProviderDetails().getTokenUri())
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .bodyValue(params)
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                .block();

        ClientResponse clientResponse = webClientBuilder.build()
                .post()
                .uri(clientRegistration.getProviderDetails().getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(params)
                .exchange() // 핵심
                .block();


        String block = clientResponse.bodyToMono(String.class).block();
        log.info("status={}", clientResponse.statusCode().value());
        log.info("headers={}", clientResponse.headers().asHttpHeaders());
        log.info("body={}", block);

        try {
            return new ObjectMapper().readTree(block).get("id_token").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        return response.get("id_token").toString();
    }

    public String start(String provider) {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);

        return  clientRegistration.getProviderDetails().getAuthorizationUri() +
                "?response_type=code" +
                "&client_id=" + clientRegistration.getClientId() +
                "&redirect_uri=" + clientRegistration.getRedirectUri() +
                "&scope=openid";
    }
}
