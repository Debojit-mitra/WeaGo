package com.bunny.weather.weago.models;

import java.util.List;

public class WeatherDataRealtime {
    private Location location;
    private Current current;
    private Forecast forecast;


    // Getters and setters for location and current

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }
    public WeatherDataRealtime() {
    }
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        private String tz_id;
        private long localtime_epoch;
        private String localtime;

        // Getters for location fields
        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getCountry() {
            return country;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public String getTz_id() {
            return tz_id;
        }

        public long getLocaltime_epoch() {
            return localtime_epoch;
        }

        public String getLocaltime() {
            return localtime;
        }
    }

    public class Current {
        private long last_updated_epoch;
        private String last_updated;
        private double temp_c;
        private double temp_f;
        private int is_day;
        private Condition condition;
        private double wind_mph;
        private double wind_kph;
        private int wind_degree;
        private String wind_dir;
        private double pressure_mb;
        private double pressure_in;
        private double precip_mm;
        private double precip_in;
        private int humidity;
        private int cloud;
        private double feelslike_c;
        private double feelslike_f;
        private double windchill_c;
        private double windchill_f;
        private double heatindex_c;
        private double heatindex_f;
        private double dewpoint_c;
        private double dewpoint_f;
        private double vis_km;
        private double vis_miles;
        private double uv;
        private double gust_mph;
        private double gust_kph;

        // Getters for current fields
        public long getLast_updated_epoch() {
            return last_updated_epoch;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public double getTemp_c() {
            return temp_c;
        }

        public double getTemp_f() {
            return temp_f;
        }

        public int getIs_day() {
            return is_day;
        }

        public Condition getCondition() {
            return condition;
        }

        public double getWind_mph() {
            return wind_mph;
        }

        public double getWind_kph() {
            return wind_kph;
        }

        public int getWind_degree() {
            return wind_degree;
        }

        public String getWind_dir() {
            return wind_dir;
        }

        public double getPressure_mb() {
            return pressure_mb;
        }

        public double getPressure_in() {
            return pressure_in;
        }

        public double getPrecip_mm() {
            return precip_mm;
        }

        public double getPrecip_in() {
            return precip_in;
        }

        public int getHumidity() {
            return humidity;
        }

        public int getCloud() {
            return cloud;
        }

        public double getFeelslike_c() {
            return feelslike_c;
        }

        public double getFeelslike_f() {
            return feelslike_f;
        }

        public double getWindchill_c() {
            return windchill_c;
        }

        public double getWindchill_f() {
            return windchill_f;
        }

        public double getHeatindex_c() {
            return heatindex_c;
        }

        public double getHeatindex_f() {
            return heatindex_f;
        }

        public double getDewpoint_c() {
            return dewpoint_c;
        }

        public double getDewpoint_f() {
            return dewpoint_f;
        }

        public double getVis_km() {
            return vis_km;
        }

        public double getVis_miles() {
            return vis_miles;
        }

        public double getUv() {
            return uv;
        }

        public double getGust_mph() {
            return gust_mph;
        }

        public double getGust_kph() {
            return gust_kph;
        }
    }

    public class Condition {
        private String text;
        private String icon;
        private int code;

        // Getters for condition fields
        public String getText() {
            return text;
        }

        public String getIcon() {
            return icon;
        }

        public int getCode() {
            return code;
        }
    }

    public class Forecast {
        private List<ForecastDay> forecastday;

        public List<ForecastDay> getForecastday() {
            return forecastday;
        }

        public void setForecastday(List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }

        public class ForecastDay {
            private String date;
            private Day day;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Day getDay() {
                return day;
            }

            public void setDay(Day day) {
                this.day = day;
            }

            public class Day {
                private double maxtemp_c;
                private double maxtemp_f;
                private double mintemp_c;
                private double mintemp_f;
                private double avgtemp_c;
                private double avgtemp_f;
                private int avghumidity;
                private Condition condition;

                public double getMaxtemp_c() {
                    return maxtemp_c;
                }

                public void setMaxtemp_c(double maxtemp_c) {
                    this.maxtemp_c = maxtemp_c;
                }

                public double getMaxtemp_f() {
                    return maxtemp_f;
                }

                public void setMaxtemp_f(double maxtemp_f) {
                    this.maxtemp_f = maxtemp_f;
                }

                public double getMintemp_c() {
                    return mintemp_c;
                }

                public void setMintemp_c(double mintemp_c) {
                    this.mintemp_c = mintemp_c;
                }

                public double getMintemp_f() {
                    return mintemp_f;
                }

                public void setMintemp_f(double mintemp_f) {
                    this.mintemp_f = mintemp_f;
                }

                public double getAvgtemp_c() {
                    return avgtemp_c;
                }

                public void setAvgtemp_c(double avgtemp_c) {
                    this.avgtemp_c = avgtemp_c;
                }

                public double getAvgtemp_f() {
                    return avgtemp_f;
                }

                public void setAvgtemp_f(double avgtemp_f) {
                    this.avgtemp_f = avgtemp_f;
                }

                public int getAvghumidity() {
                    return avghumidity;
                }

                public void setAvghumidity(int avghumidity) {
                    this.avghumidity = avghumidity;
                }

                public Condition getCondition() {
                    return condition;
                }

                public void setCondition(Condition condition) {
                    this.condition = condition;
                }

                public class Condition {
                    private String text;
                    private String icon;
                    private int code;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }

                    public int getCode() {
                        return code;
                    }

                    public void setCode(int code) {
                        this.code = code;
                    }
                }
            }
        }
    }
}
