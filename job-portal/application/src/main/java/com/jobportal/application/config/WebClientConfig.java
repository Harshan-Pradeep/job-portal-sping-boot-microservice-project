package com.jobportal.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.gateway.url:http://localhost:8085}")
    private String apiGatewayUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiGatewayUrl)
                .build();
    }
}