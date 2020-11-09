package cn.benbenedu.sundial.broadcast.configuration;

import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProductCode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("custom.credit-ease")
@Data
public class CreditEaseConfiguration {

    private List<Product> products;
    private String signKey;
    private String clientId;
    private String clientSecret;
    private String accountName;
    private String accountPassword;

    @Data
    public static class Product {

        private CreditEaseProductCode code;
        private String echainTitle;
        private String examTitle;
        private String assessCodeTag;
        private String notifyPhase;
    }
}
