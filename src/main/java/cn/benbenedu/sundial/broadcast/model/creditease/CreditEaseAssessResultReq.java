package cn.benbenedu.sundial.broadcast.model.creditease;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreditEaseAssessResultReq {

    @NotNull
    private CreditEaseProductCode productId;

    @NotNull
    private String assessCode;

    @NotNull
    private String token;

    @NotNull
    private String timestamp;
}
