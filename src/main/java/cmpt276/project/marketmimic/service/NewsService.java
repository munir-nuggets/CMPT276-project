package cmpt276.project.marketmimic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import cmpt276.project.marketmimic.model.NewsResponse;

import org.slf4j.Logger;


@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Value("${marketaux.api.key}")
    private String apiKey; // Replace with your actual API key

    public NewsResponse getNewsForSymbol(String symbol) {
        logger.info("Fetching news for symbol: {}", symbol);
        try {
            String urlString = "https://api.marketaux.com/v1/news/all?symbols=" + symbol + "&api_token=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray jsonData = jsonResponse.getJSONArray("data");
                List<NewsResponse.NewsArticle> articles = new ArrayList<>();

                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject articleJson = jsonData.getJSONObject(i);
                    NewsResponse.NewsArticle article = new NewsResponse.NewsArticle();
                    article.setUuid(articleJson.getString("uuid"));
                    article.setTitle(articleJson.getString("title"));
                    article.setDescription(articleJson.getString("description"));
                    article.setSnippet(articleJson.getString("snippet"));
                    article.setUrl(articleJson.getString("url"));
                    article.setImage_url(articleJson.getString("image_url"));
                    article.setLanguage(articleJson.getString("language"));
                    article.setPublished_at(articleJson.getString("published_at"));
                    article.setSource(articleJson.getString("source"));
                    articles.add(article);
                }

                NewsResponse newsResponse = new NewsResponse();
                newsResponse.setData(articles);
                logger.info("Successfully fetched {} news articles for symbol: {}", articles.size(), symbol);
                return newsResponse;
            } else {
                logger.error("Failed to fetch news, HTTP response code: {}", responseCode);
                throw new RuntimeException("Failed to fetch news, HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching news: {}", e.getMessage());
            throw new RuntimeException("Error occurred while fetching news: " + e.getMessage());
        }
    }
}
