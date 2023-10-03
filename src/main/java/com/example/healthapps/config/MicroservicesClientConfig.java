package com.example.healthapps.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MicroservicesClientConfig {

    @Value("${service.default.url}")
    private String serviceDefaultUrl;

//    @Value("${service.name.url}")
//    private String serviceNameUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getServiceDefaultUrl() {
        return serviceDefaultUrl;
    }
}
