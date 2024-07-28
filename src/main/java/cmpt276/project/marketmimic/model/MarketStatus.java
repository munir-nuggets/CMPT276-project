package cmpt276.project.marketmimic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketStatus {
    private String holiday;
    private boolean isOpen;

    public String getHoliday() {
        return holiday;
    }
    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
    public boolean getIsOpen() {
        return isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
