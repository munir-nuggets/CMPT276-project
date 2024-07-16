package cmpt276.project.marketmimic.model;

import java.util.List;

public class StockDataResponse {
    private String ticker;
    private List<StockData> results;

    public static class StockData {
        private long t;
        private double c;

        public long getT() {
            return t;
        }
        public void setT(long t) {
            this.t = t;
        }
        public double getC() {
            return c;
        }
        public void setC(double c) {
            this.c = c;
        }

    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<StockData> getResults() {
        return results;
    }

    public void setResults(List<StockData> results) {
        this.results = results;
    }
}