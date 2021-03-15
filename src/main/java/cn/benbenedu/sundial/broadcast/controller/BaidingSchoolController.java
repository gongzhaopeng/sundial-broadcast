package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.configuration.BaidingSchoolConfiguration;
import cn.benbenedu.sundial.broadcast.dto.GeneralLoginReq;
import cn.benbenedu.sundial.broadcast.dto.GeneralLoginResp;
import cn.benbenedu.sundial.broadcast.exception.ErrorResponseException;
import cn.benbenedu.sundial.broadcast.model.ErrorResponse;
import cn.benbenedu.sundial.broadcast.model.ErrorResponseCode;
import cn.benbenedu.sundial.broadcast.service.AssessCodeService;
import cn.benbenedu.sundial.broadcast.service.BaidingSchoolService;
import cn.benbenedu.sundial.broadcast.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/genesis/baiding-school")
@Slf4j
public class BaidingSchoolController {

    private final BaidingSchoolConfiguration baidingSchoolConfiguration;
    private final LoginService loginService;
    private final BaidingSchoolService baidingSchoolService;
    private final AssessCodeService assessCodeService;

    public BaidingSchoolController(
            final BaidingSchoolConfiguration baidingSchoolConfiguration,
            final LoginService loginService,
            final BaidingSchoolService baidingSchoolService,
            final AssessCodeService assessCodeService) {

        this.baidingSchoolConfiguration = baidingSchoolConfiguration;
        this.loginService = loginService;
        this.baidingSchoolService = baidingSchoolService;
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
                baidingSchoolConfiguration.getClientId(), baidingSchoolConfiguration.getClientSecret(),
                baidingSchoolService.getAccount().getId(), baidingSchoolConfiguration.getAccountPassword());

        final var resp = new GeneralLoginResp();
        resp.setAccessToken(ret.getAccessToken());

        return resp;
    }
}
