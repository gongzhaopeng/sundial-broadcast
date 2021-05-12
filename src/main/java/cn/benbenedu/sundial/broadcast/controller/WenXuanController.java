package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.configuration.WenXuanConfiguration;
import cn.benbenedu.sundial.broadcast.dto.GeneralLoginReq;
import cn.benbenedu.sundial.broadcast.dto.GeneralLoginResp;
import cn.benbenedu.sundial.broadcast.exception.ErrorResponseException;
import cn.benbenedu.sundial.broadcast.model.ErrorResponse;
import cn.benbenedu.sundial.broadcast.model.ErrorResponseCode;
import cn.benbenedu.sundial.broadcast.service.AssessCodeService;
import cn.benbenedu.sundial.broadcast.service.LoginService;
import cn.benbenedu.sundial.broadcast.service.WenXuanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/genesis/wenxuan")
@Slf4j
public class WenXuanController {

    private final WenXuanConfiguration wenXuanConfiguration;
    private final LoginService loginService;
    private final WenXuanService wenXuanService;
    private final AssessCodeService assessCodeService;

    public WenXuanController(
            final WenXuanConfiguration wenXuanConfiguration,
            final LoginService loginService,
            final WenXuanService wenXuanService,
            final AssessCodeService assessCodeService) {

        this.wenXuanConfiguration = wenXuanConfiguration;
        this.loginService = loginService;
        this.wenXuanService = wenXuanService;
        this.assessCodeService = assessCodeService;
    }

    @PostMapping("/login")
    public GeneralLoginResp login(
            @RequestBody @Valid GeneralLoginReq generalLoginReq) {

        if (!assessCodeService.verifyAssessCode(
                generalLoginReq.getTargetType(), generalLoginReq.getTargetId(), generalLoginReq.getAssessCode())) {
            final var errorResponse =
                    new ErrorResponse(ErrorResponseCode.InvalidReqParams);
            errorResponse.setDetail("Invalid assessCode.");
            throw new ErrorResponseException(errorResponse);
        }

        final var ret = loginService.acquireAccessToken(
                wenXuanConfiguration.getClientId(), wenXuanConfiguration.getClientSecret(),
                wenXuanService.getAccount().getId(), wenXuanConfiguration.getAccountPassword());

        final var resp = new GeneralLoginResp();
        resp.setAccessToken(ret.getAccessToken());

        return resp;
    }
}
