package cn.benbenedu.sundial.broadcast.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {

        final var restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Bean
    @Pure
    public RestTemplate restTemplate() {

        final var restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Target({
            ElementType.CONSTRUCTOR,
            ElementType.FIELD,
            ElementType.METHOD,
            ElementType.TYPE,
            ElementType.PARAMETER
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface Pure {
    }
}
