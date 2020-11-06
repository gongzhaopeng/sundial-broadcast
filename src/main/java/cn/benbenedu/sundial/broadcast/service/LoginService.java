package cn.benbenedu.sundial.broadcast.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {

    private static final String OAUTH_TOKEN_URL = "http://xauth/oauth/token";

    private final RestTemplate restTemplate;

    public LoginService(final RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public AccessTokenAcquiringRet acquireAccessToken(
            final String clientId,
            final String clientSecret,
            final String userName,
            final String password) {

        final var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization",
                "Basic " + Base64Utils.encodeToString((clientId + ":" + clientSecret).getBytes()));

        final var params = new LinkedMultiValueMap<String, String>();
        params.add("grant_type", "password");
        params.add("scope", "client:partner");
        params.add("username", userName);
        params.add("password", password);

        final var httpEntity = new HttpEntity<>(params, httpHeaders);

        return restTemplate.postForObject(
                OAUTH_TOKEN_URL, httpEntity, AccessTokenAcquiringRet.class);
    }

    @Data
    public static class AccessTokenAcquiringRet {

        @JsonProperty("access_token")
        private String accessToken;
    }
}
