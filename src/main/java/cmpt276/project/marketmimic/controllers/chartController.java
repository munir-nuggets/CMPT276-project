// package cmpt276.project.marketmimic.controllers;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import cmpt276.project.marketmimic.service.*;
// import cmpt276.project.marketmimic.model.StockDataResponse;

// import java.io.IOException;

// @Controller
// public class chartController {

//     private final PolygonService polygonService;
//     private final ChartService chartService;

//     public chartController(PolygonService polygonService, ChartService chartService) {
//         this.polygonService = polygonService;
//         this.chartService = chartService;
//     }

//     @GetMapping("/stock/chart")
//     public String getStockChart(@RequestParam String ticker) {
//         StockDataResponse stockDataResponse = polygonService.getStockData(ticker);
//         try {
//             chartService.createStockChart(stockDataResponse, ticker);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return "Error creating chart";
//         }
//         return "Chart created successfully";
//     }
// }