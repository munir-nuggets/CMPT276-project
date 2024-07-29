package cmpt276.project.marketmimic.model;

import java.time.LocalDate;
import java.util.List;

public class PendingTradeStockData {
    private String symbol;
    private List<StockData> historical;

    public static class StockData {
        private double open;
        private LocalDate date;

        public double getOpen() {
            return open;
        }
        public void setOpen(double open) {
            this.open = open;
        }
        public LocalDate getDate() {
            return date;
        }
        public void setDate(LocalDate date) {
            this.date = date;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<StockData> getHistorical() {
        return historical;
    }

    public void setResults(List<StockData> historical) {
        this.historical = historical;
    }
}
