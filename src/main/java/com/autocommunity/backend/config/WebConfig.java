package com.autocommunity.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebFluxConfigurer corsConfigurer(
        @Value("${web.front-address}") String frontAddress
    ) {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(frontAddress).allowCredentials(true);
            }
        };
    }

    @Bean
    public Boolean httpsEnabled(
        @Value("${web.https-enabled}") Boolean httpsEnabled
    ) {
        return httpsEnabled;
    }
}
