package cn.benbenedu.sundial.broadcast.model.creditease;

import lombok.Data;

@Data
public class CreditEaseAssessResult {

    private String respCode;
    private String respMessage;

    private String productId;
    private String assessTime;
    private String assessCode;
    private String assessResultUrl;
}
