package com.example.wigelltravels_.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ClientConfig {

    @Value("${pricing.username}")
    private String pricingUser;

    @Value("${pricing.password}")
    private String pricingPass;

    @Bean
    public RestTemplate pricingRestTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(5))
                .basicAuthentication(pricingUser, pricingPass)
                .build();
    }
}
