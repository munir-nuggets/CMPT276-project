package cmpt276.project.marketmimic.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;

import cmpt276.project.marketmimic.model.StockDataResponse;

import java.awt.BasicStroke;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class ChartService {

    public void createStockChart(StockDataResponse stockDataResponse, String ticker) throws IOException {
        TimeSeries series = new TimeSeries("Price");

        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;

        for (StockDataResponse.StockData data : stockDataResponse.getResults()) {
            Date date = new Date(data.getT());
            double price = data.getC();
            series.add(new Day(date), price);

            if (price < minPrice) {
                minPrice = price;
            }
            if (price > maxPrice) {
                maxPrice = price;
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                ticker,
                "Date",
                "Price",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        double buffer = (maxPrice - minPrice) * 0.05;
        rangeAxis.setRange(minPrice - buffer, maxPrice + buffer);

        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
        dateAxis.setLabel("");

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        Font axisFont = new Font("SansSerif", Font.BOLD, 14);
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setTickLabelFont(axisFont);
        dateAxis.setTickLabelFont(axisFont);

        int width = 1280;
        int height = 720;
        File chartFile = new File("StockChart.png");
        ChartUtils.saveChartAsPNG(chartFile, chart, width, height);
    }
}