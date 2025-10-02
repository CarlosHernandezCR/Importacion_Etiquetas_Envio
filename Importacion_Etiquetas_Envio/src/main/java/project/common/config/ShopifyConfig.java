package project.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.common.constantes.ShopifyConstants;

@Data
@Configuration
@ConfigurationProperties(prefix = "shopify.api")
public class ShopifyConfig {
    private String shop;
    private String apiVersion;
    private String accessToken;
    private String key;
    private String secret;
    private String scopes;
    private String redirectUri;

    public String base() {
        return ShopifyConstants.HTTPS + shop + ShopifyConstants.ADMIN_API + apiVersion;
    }
}
