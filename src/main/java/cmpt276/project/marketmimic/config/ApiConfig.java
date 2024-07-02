package cmpt276.project.marketmimic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "finnhub")
public class ApiConfig {
    private String finnhubApiKey = "cq1jlm1r01qjh3d5pt90cq1jlm1r01qjh3d5pt9g";
    private String polygonApiKey = "oKU_VAKwXYjlkUoMeB2CodWt1hQmCYI3";

    public String getFinnhubApiKey() {
        return finnhubApiKey;
    }

    public String getPolygonApiKey() {
        return polygonApiKey;
    }
}
