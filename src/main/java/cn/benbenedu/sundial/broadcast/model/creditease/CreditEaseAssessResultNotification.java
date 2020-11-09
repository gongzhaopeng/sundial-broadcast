package cn.benbenedu.sundial.broadcast.model.creditease;

import lombok.Data;

@Data
public class CreditEaseAssessResultNotification {

    private String productId;
    private String assessTime;
    private String assessCode;
    private String assessResultUrl;
    private String token;
    private String timestamp;
}
