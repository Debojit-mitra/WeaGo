package com.bunny.weather.weago.models;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class AQIResponseModel {

    @SerializedName("current")
    public Current current;

    public static class Current {
        @SerializedName("european_aqi")
        public int europeanAqi;

        public int getEuropeanAqi() {
            return europeanAqi;
        }

        public void setEuropeanAqi(int europeanAqi) {
            this.europeanAqi = europeanAqi;
        }
    }

    public static class DataDeserializer implements JsonDeserializer<Current> {
        @Override
        public Current deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                return new Current(); // Return an empty Current object or handle as needed
            }

            JsonObject jsonObject = json.getAsJsonObject();
            Current current = new Current();
            if (jsonObject.has("european_aqi")) {
                JsonElement aqiElement = jsonObject.get("european_aqi");
                if (aqiElement.isJsonPrimitive() && aqiElement.getAsJsonPrimitive().isNumber()) {
                    current.setEuropeanAqi(aqiElement.getAsInt());
                } else {
                    current.setEuropeanAqi(-1);
                }
            }
            return current;
        }
    }
}
