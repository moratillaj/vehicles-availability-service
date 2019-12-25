package com.meep.vehicles.availability.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class VehiclesAvailabilityConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
