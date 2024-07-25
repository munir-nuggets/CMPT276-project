package cmpt276.project.marketmimic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.NewsResponse;
import cmpt276.project.marketmimic.service.NewsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsResponseService;

    @GetMapping("/news")
    public String getNews(@RequestParam(value = "symbol", defaultValue = "AAPL") String symbol, Model model) {
        logger.info("Request received for news with symbol: {}", symbol);
        try {
            NewsResponse newsResponse = newsResponseService.getNewsForSymbol(symbol);
            model.addAttribute("newsResponse", newsResponse);
            model.addAttribute("symbol", symbol);
            return "news"; // Name of your Thymeleaf template
        } catch (Exception e) {
            logger.error("Error while fetching news: {}", e.getMessage());
            model.addAttribute("error", "Failed to fetch news. Please try again later.");
            return "news"; // You may want to redirect or render a different page on error
        }
    }
}