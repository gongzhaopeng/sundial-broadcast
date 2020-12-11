package cn.benbenedu.sundial.broadcast.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
@Slf4j
public class CustomPortConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Value("${port.number.min:1}")
    private Integer minPort;

    @Value("${port.number.max:65535}")
    private Integer maxPort;

    @Value("${server.port}")
    private String initialServerPort;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {

        if (!initialServerPort.equals("0")) {
            return;
        }

        final var port = SocketUtils.findAvailableTcpPort(minPort, maxPort);
        log.info("Port:0 is configured initially, so valid-port:{} will be assigned.", port);

        factory.setPort(port);
        System.getProperties().put("server.port", port);
    }
}
