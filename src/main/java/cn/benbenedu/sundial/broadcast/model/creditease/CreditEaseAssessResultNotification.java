package cn.benbenedu.sundial.broadcast.model.creditease;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreditEaseAssessResultNotification {

    private CreditEaseProductCode productId;
    private String assessTime;
    private String assessCode;
    private String assessResultUrl;
    private String token;
    private String timestamp;

    private String orderCode;
}
