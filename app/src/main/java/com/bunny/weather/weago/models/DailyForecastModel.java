package com.bunny.weather.weago.models;

public class DailyForecastModel {
    private String date;
    private double avgTempC;
    private double minTempC;
    private double maxTempC;
    private int avgHumidity;
    private String conditionText;
    private String conditionIcon;

    public DailyForecastModel() {
    }

    // Constructor
    public DailyForecastModel(String date, double avgTempC, double minTempC, double maxTempC, int avgHumidity, String conditionText, String conditionIcon) {
        this.date = date;
        this.avgTempC = avgTempC;
        this.minTempC = minTempC;
        this.maxTempC = maxTempC;
        this.avgHumidity = avgHumidity;
        this.conditionText = conditionText;
        this.conditionIcon = conditionIcon;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAvgTempC() {
        return avgTempC;
    }

    public void setAvgTempC(double avgTempC) {
        this.avgTempC = avgTempC;
    }

    public double getMinTempC() {
        return minTempC;
    }

    public void setMinTempC(double minTempC) {
        this.minTempC = minTempC;
    }

    public double getMaxTempC() {
        return maxTempC;
    }

    public void setMaxTempC(double maxTempC) {
        this.maxTempC = maxTempC;
    }

    public int getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(int avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(String conditionIcon) {
        this.conditionIcon = conditionIcon;
    }
}
