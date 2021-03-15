package cn.benbenedu.sundial.broadcast.dto;

import cn.benbenedu.sundial.broadcast.model.AssessTokenTargetType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GeneralLoginReq {

    @NotNull
    private AssessTokenTargetType targetType;

    @NotNull
    private String targetId;

    @NotNull
    private String assessCode;
}
