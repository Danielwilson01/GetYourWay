package com.sky.getyourway;

import com.duffel.DuffelApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${api.key}")
    private String apiKey;

    @Bean
    DuffelApiClient duffelApiClient() {
        return new DuffelApiClient(apiKey);
    }
}
