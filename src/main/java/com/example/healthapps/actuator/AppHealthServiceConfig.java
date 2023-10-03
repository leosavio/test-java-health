package com.example.healthapps.actuator;

import com.example.healthapps.config.MicroservicesClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppHealthServiceConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MicroservicesClientConfig microservicesClientConfig;

    @Bean(name = "defaultHealthIndicator")
    public HealthIndicator defaultHealthIndicator() {
        return () -> {
            try {
                HealthStatus serviceDefault = restTemplate.getForObject(
                        microservicesClientConfig.getServiceDefaultUrl() + "/actuator/health/readiness",
                        HealthStatus.class
                );
                if (Status.UP.getCode().equalsIgnoreCase(serviceDefault.getStatus())) {
                    return Health.up().build();
                } else {
                    return Health.outOfService()
                            .withDetail("reason", "Default service is not ready!")
                            .build();
                }
            } catch (ResourceAccessException e) {
                if (e.getMessage().contains("Connection refused")) {
                    return Health.outOfService()
                            .withDetail("reason", "Connection refused by Default Service")
                            .build();
                } else {
                    // Handle other connection errors
                    return Health.outOfService()
                            .withDetail("reason", "Error occurred during the call Default Service readinessProbe: " + e.getMessage())
                            .build();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return Health.down()
                        .withDetail("reason", "Error occurred during the call Default Service readinessProbe: " + e.getMessage())
                        .build();
            }
        };
    }

}
