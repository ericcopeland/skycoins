package com.copelando.skycoins.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfiguration {
    private final Environment environment;

    @Autowired
    public ApiConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public WebClient hypixelApiClient() {
        String baseUrl = environment.getProperty("hypixel.api.baseUrl");
        return WebClient.builder().exchangeStrategies(
                ExchangeStrategies.builder().codecs(
                        codecs -> codecs.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)
                ).build()
        ).baseUrl(baseUrl).build();
    }
}
