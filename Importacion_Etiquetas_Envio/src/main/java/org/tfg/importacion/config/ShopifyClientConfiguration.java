package org.tfg.importacion.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ShopifyProperties.class)
public class ShopifyClientConfiguration {

    @Bean
    public WebClient shopifyWebClient(WebClient.Builder builder, ShopifyProperties properties) {
        return builder
                .baseUrl(properties.getApiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Shopify-Access-Token", properties.getAccessToken())
                .build();
    }
}
