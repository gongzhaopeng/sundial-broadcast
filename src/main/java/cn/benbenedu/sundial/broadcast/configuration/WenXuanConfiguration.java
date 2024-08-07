package cn.benbenedu.sundial.broadcast.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("custom.wenxuan")
@Data
public class WenXuanConfiguration {

    private String clientId;
    private String clientSecret;
    private String accountName;
    private String accountPassword;
}
