package cmpt276.project.marketmimic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "finnhub")
public class ApiConfig {
    private String apiKey = "cq1jlm1r01qjh3d5pt90cq1jlm1r01qjh3d5pt9g";

    public String getApiKey() {
        return apiKey;
    }
}
