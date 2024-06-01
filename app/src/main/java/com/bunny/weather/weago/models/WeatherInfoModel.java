package com.bunny.weather.weago.models;

public class WeatherInfoModel {

    private int iconResId;
    private String label;
    private String value;
    public WeatherInfoModel() {
    }
    public WeatherInfoModel(int iconResId, String label, String value) {
        this.iconResId = iconResId;
        this.label = label;
        this.value = value;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
