package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProductCode;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/genesis/credit-ease")
public class CreditEaseController {

    @PostMapping("/login")
    public LoginResp login(
            @RequestBody @Valid LoginReq loginReq) {

        // TODO
        return null;
    }

    @PostMapping("/login/simple")
    public LoginResp simpleLogin(
            @RequestBody @Valid SimpleLoginReq simpleLoginReq) {

        // TODO
        return null;
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
        private String timestamp;
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
        private String access_token;
    }
}
