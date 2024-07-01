package cmpt276.project.marketmimic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "finnhub")
public class ApiConfig {
    private String apiKey = "cpma4jhr01quf620pvkgcpma4jhr01quf620pvl0";

    public String getApiKey() {
        return apiKey;
    }
}
