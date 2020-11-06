package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.configuration.CreditEaseConfiguration;
import cn.benbenedu.sundial.broadcast.exception.ErrorResponseException;
import cn.benbenedu.sundial.broadcast.model.ErrorResponse;
import cn.benbenedu.sundial.broadcast.model.ErrorResponseCode;
import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProductCode;
import cn.benbenedu.sundial.broadcast.service.CreditEaseService;
import cn.benbenedu.sundial.broadcast.service.LoginService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping("/genesis/credit-ease")
public class CreditEaseController {

    private final CreditEaseConfiguration creditEaseConfiguration;
    private final LoginService loginService;
    private final CreditEaseService creditEaseService;

    public CreditEaseController(
            final CreditEaseConfiguration creditEaseConfiguration,
            final LoginService loginService,
            final CreditEaseService creditEaseService) {

        this.creditEaseConfiguration = creditEaseConfiguration;
        this.loginService = loginService;
        this.creditEaseService = creditEaseService;
    }

    @PostMapping("/login")
    public LoginResp login(
            @RequestBody @Valid LoginReq loginReq) {

        final var params = Map.<String, Object>of(
                "productId", loginReq.getProductId(),
                "assessCode", loginReq.getAssessCode());

        if (!creditEaseService.verifySign(params, loginReq.getTimestamp(), loginReq.getToken())) {
            final var errorResponse =
                    new ErrorResponse(ErrorResponseCode.InvalidReqParams);
            errorResponse.setDetail("Invalid token.");
            throw new ErrorResponseException(errorResponse);
        }

        if (!creditEaseService.verifyAssessCode(loginReq.getProductId(), loginReq.getAssessCode())) {
            final var errorResponse =
                    new ErrorResponse(ErrorResponseCode.InvalidReqParams);
            errorResponse.setDetail("Invalid assessCode.");
            throw new ErrorResponseException(errorResponse);
        }

        return doLogin();
    }

    @PostMapping("/login/simple")
    public LoginResp simpleLogin(
            @RequestBody @Valid SimpleLoginReq simpleLoginReq) {

        if (!creditEaseService.verifyAssessCode(simpleLoginReq.getProductId(), simpleLoginReq.getAssessCode())) {
            final var errorResponse =
                    new ErrorResponse(ErrorResponseCode.InvalidReqParams);
            errorResponse.setDetail("Invalid assessCode.");
            throw new ErrorResponseException(errorResponse);
        }

        return doLogin();
    }

    @PostMapping("/assess-code/signing")
    public String signAssessCode(@RequestBody @Valid AssessCodeSigningReq req) {

        final var params = Map.<String, Object>of(
                "productId", req.getProductId(),
                "assessCode", req.getAssessCode());

        return creditEaseService.sign(params, req.getTimestamp());
    }

    private LoginResp doLogin() {

        final var ret = loginService.acquireAccessToken(
                creditEaseConfiguration.getClientId(), creditEaseConfiguration.getClientSecret(),
                creditEaseConfiguration.getAccountName(), creditEaseConfiguration.getAccountPassword());

        final var resp = new LoginResp();
        resp.setAccessToken(ret.getAccessToken());

        return resp;
    }

    @Data
    static class LoginReq {

        @NotNull
        private CreditEaseProductCode productId;

        @NotNull
        private String assessCode;

        @NotNull
        private String token;

        @NotNull
        private Long timestamp;
    }

    @Data
    static class SimpleLoginReq {

        @NotNull
        private CreditEaseProductCode productId;

        @NotNull
        private String assessCode;
    }

    @Data
    static class LoginResp {

        @NotNull
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Data
    static class AssessCodeSigningReq {

        @NotNull
        private CreditEaseProductCode productId;

        @NotNull
        private String assessCode;

        @NotNull
        private Long timestamp;
    }
}
