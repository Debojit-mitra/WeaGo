package com.bunny.weather.weago.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunny.weather.WeaGo.BuildConfig;
import com.bunny.weather.WeaGo.R;
import com.bunny.weather.weago.adapters.DailyForecastAdapter;
import com.bunny.weather.weago.adapters.WeatherInfoAdapter;
import com.bunny.weather.weago.models.AQIResponseModel;
import com.bunny.weather.weago.models.DailyForecastModel;
import com.bunny.weather.weago.models.WeatherDataRealtime;
import com.bunny.weather.weago.models.WeatherInfoModel;
import com.bunny.weather.weago.utils.Extras;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.WEATHER_API_KEY;
    private static final String BASE_URL = "http://api.weatherapi.com/v1/forecast.json";
    private static final String BASE_URL_AQI = "https://air-quality-api.open-meteo.com/v1/air-quality?";
    private OkHttpClient client;
    private Gson gson;
    private FusedLocationProviderClient fusedLocationClient;
    LinearLayout linear_1_getting_location, linear_2_location;
    RelativeLayout relative_3_current_temp;
    TextView textView_location_name, textView_current_temp, textView_feels_like, textView_date, textView_condition, textView_last_update,
            textView_7_day_forecast, textView_additional_info;
    ImageView imageView_condition;
    RecyclerView recyclerView_extra_info, recyclerView_forecast;
    List<WeatherInfoModel> weatherInfoModelList;
    List<DailyForecastModel> dailyForecastsList;
    WeatherInfoAdapter weatherInfoAdapter;
    DailyForecastAdapter dailyForecastAdapter;
    AQIResponseModel aqiResponseModel;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Bugsnag.notify(new RuntimeException("Test error"));

        initializeViews();

        client = new OkHttpClient();
        gson = new GsonBuilder().registerTypeAdapter(AQIResponseModel.Current.class, new AQIResponseModel.DataDeserializer()).create();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationPermission();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textView_last_update.postDelayed(() -> fadeOutAndHideLinearLayout(textView_last_update), 50);
                relative_3_current_temp.postDelayed(() -> fadeOutAndHideLinearLayout(relative_3_current_temp), 100);
                textView_7_day_forecast.postDelayed(() -> fadeOutAndHideLinearLayout(textView_7_day_forecast), 150);
                recyclerView_forecast.postDelayed(() -> fadeOutAndHideLinearLayout(recyclerView_forecast), 200);
                textView_additional_info.postDelayed(() -> fadeOutAndHideLinearLayout(textView_additional_info), 250);
                recyclerView_extra_info.postDelayed(() -> fadeOutAndHideLinearLayout(recyclerView_extra_info), 300);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLastLocation();
                    }
                }, 500);
            }
        });

        // Handle the back button press
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish all activities and clear the task to exit the app
                finish();
                System.exit(0);
            }
        });

    }

    private void initializeViews() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        linear_1_getting_location = findViewById(R.id.linear_1_getting_location);
        linear_2_location = findViewById(R.id.linear_2_location);
        relative_3_current_temp = findViewById(R.id.relative_3_current_temp);
        textView_location_name = findViewById(R.id.textView_location_name);
        textView_current_temp = findViewById(R.id.textView_current_temp);
        textView_feels_like = findViewById(R.id.textView_feels_like);
        imageView_condition = findViewById(R.id.imageView_condition);
        textView_condition = findViewById(R.id.textView_condition);
        textView_date = findViewById(R.id.textView_date);
        recyclerView_extra_info = findViewById(R.id.recyclerView_extra_info);
        textView_last_update = findViewById(R.id.textView_last_update);
        recyclerView_forecast = findViewById(R.id.recyclerView_forecast);
        textView_7_day_forecast = findViewById(R.id.textView_7_day_forecast);
        textView_additional_info = findViewById(R.id.textView_additional_info);
    }

    private void requestLocationPermission() {
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        getLastLocation();
                    } else {
                        Log.e("MainActivity", "Location permission denied");
                    }
                });
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MainActivity", "Location permission not granted");
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        getWeatherData(location.getLatitude(), location.getLongitude());
                    } else {
                        Log.e("MainActivity", "Location is null");
                    }
                });
    }


    private void getWeatherData(double latitude, double longitude) {
        String url = BASE_URL + "?key=" + API_KEY + "&q=" + latitude + "," + longitude + "&days=7";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("WeatherFetcher", "Failed to fetch weather data", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("WeatherFetcher", "Unexpected code " + response);
                    if (response.body() != null) {
                        response.body().close();
                    }
                    return;
                }

                String responseData = Objects.requireNonNull(response.body()).string();
                WeatherDataRealtime weatherData = gson.fromJson(responseData, WeatherDataRealtime.class);

                if (weatherData != null) { // Add this null check
                    getAqi(weatherData);
                    setWeatherData(weatherData);
                } else {
                    // Handle the case where weatherData is null (e.g., display an error message)
                    Log.e("MainActivity", "WeatherDataRealtime object is null in onResponse");
                }

                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

    private void getAqi(WeatherDataRealtime weatherData) {
        if (weatherInfoModelList == null) {
            weatherInfoModelList = new ArrayList<>();
        }

        String url = BASE_URL_AQI + "latitude=" + weatherData.getLocation().getLat() + "&longitude=" + weatherData.getLocation().getLon() + "&current=european_aqi";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("AQIFetcher", "Failed to fetch AQI data", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("AQIFetcher", "Unexpected code " + response);
                    if (response.body() != null) {
                        response.body().close();
                    }
                    return;
                }

                String responseData = Objects.requireNonNull(response.body()).string();
                try {
                    aqiResponseModel = gson.fromJson(responseData, AQIResponseModel.class);
                    int aqi = aqiResponseModel.current.getEuropeanAqi();

                    String aqiText = "AQI " + aqi;
                    String aqiStatus = AQIStatus.getStatus(aqi);
                    weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_air_quality, aqiText, aqiStatus));

                    if (weatherInfoAdapter != null) {
                        new Handler(getMainLooper()).post(() -> {
                            recyclerView_extra_info.setItemAnimator(new DefaultItemAnimator());
                            weatherInfoAdapter.notifyItemInserted(weatherInfoModelList.size());
                        });
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("AQIFetcher", "Failed to parse AQI data", e);
                }

                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

    private void setWeatherData(WeatherDataRealtime weatherData) {

        // Log location data
        WeatherDataRealtime.Location location = weatherData.getLocation();
        // Log current weather data
        WeatherDataRealtime.Current current = weatherData.getCurrent();
        // Log condition data
        WeatherDataRealtime.Condition condition = current.getCondition();

      /*  //logging
        Log.d("WeatherFetcher", "Location: " + location.getName() + ", " + location.getRegion() + ", " + location.getCountry());
        Log.d("WeatherFetcher", "Latitude: " + location.getLat() + ", Longitude: " + location.getLon());
        Log.d("WeatherFetcher", "Timezone ID: " + location.getTz_id());
        Log.d("WeatherFetcher", "Local Time: " + location.getLocaltime());


        Log.d("WeatherFetcher", "Last Updated: " + current.getLast_updated());
        Log.d("WeatherFetcher", "Temperature: " + current.getTemp_c() + "°C (" + current.getTemp_f() + "°F)");
        Log.d("WeatherFetcher", "Condition: " + current.getCondition().getText());
        Log.d("WeatherFetcher", "Humidity: " + current.getHumidity() + "%");
        Log.d("WeatherFetcher", "Wind Speed: " + current.getWind_kph() + " km/h (" + current.getWind_mph() + " mph)");
        Log.d("WeatherFetcher", "Pressure: " + current.getPressure_mb() + " mb (" + current.getPressure_in() + " in)");
        Log.d("WeatherFetcher", "Precipitation: " + current.getPrecip_mm() + " mm (" + current.getPrecip_in() + " in)");
        Log.d("WeatherFetcher", "Cloud: " + current.getCloud() + "%");
        Log.d("WeatherFetcher", "Feels Like: " + current.getFeelslike_c() + "°C (" + current.getFeelslike_f() + "°F)");
        Log.d("WeatherFetcher", "Wind Chill: " + current.getWindchill_c() + "°C (" + current.getWindchill_f() + "°F)");
        Log.d("WeatherFetcher", "Heat Index: " + current.getHeatindex_c() + "°C (" + current.getHeatindex_f() + "°F)");
        Log.d("WeatherFetcher", "Dewpoint: " + current.getDewpoint_c() + "°C (" + current.getDewpoint_f() + "°F)");
        Log.d("WeatherFetcher", "Visibility: " + current.getVis_km() + " km (" + current.getVis_miles() + " miles)");
        Log.d("WeatherFetcher", "UV Index: " + current.getUv());
        Log.d("WeatherFetcher", "Wind Gust: " + current.getGust_kph() + " km/h (" + current.getGust_mph() + " mph)");


        Log.d("WeatherFetcher", "Condition Text: " + condition.getText());
        Log.d("WeatherFetcher", "Condition Icon: " + condition.getIcon());
        Log.d("WeatherFetcher", "Condition Code: " + condition.getCode());*/

        weatherInfoModelList = new ArrayList<>();
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_humidity, "Humidity", current.getHumidity() + "%"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_wind, "Wind Speed", current.getWind_kph() + " km/h"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_pressure, "Pressure", current.getPressure_mb() + " mb"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_precipitation, "Precipitation", current.getPrecip_mm() + " mm"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_cloud, "Cloud", current.getCloud() + "%"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_dewpoint, "Dewpoint", current.getDewpoint_c() + "°C"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_visibility, "Visibility", current.getVis_km() + " km"));
        weatherInfoModelList.add(new WeatherInfoModel(R.drawable.ic_uv_index, "UV Index", String.valueOf(current.getUv())));

        dailyForecastsList = processForecastData(weatherData.getForecast().getForecastday());


        runOnUiThread(() -> {
            fadeOutAndHideLinearLayout(linear_1_getting_location);

            textView_location_name.setText(location.getName());

            SpannableString formattedDate = Extras.formatDateString(location.getLocaltime());
            textView_date.setText(formattedDate);

            String lastUpdated = "Last Updated: " + Extras.getTimeFromString(current.getLast_updated());
            textView_last_update.setText(lastUpdated);

            String curTemp = current.getTemp_c() + "°c";
            textView_current_temp.setText(curTemp);

            String feelsLike = "Feels like " + current.getFeelslike_c() + "°c";
            textView_feels_like.setText(feelsLike);

            String temp_image = condition.getIcon();
            if (temp_image.contains("64x64")) {
                temp_image = "https:" + temp_image.replace("64x64", "128x128");
            } else {
                temp_image = "https:" + temp_image;
            }
            Glide.with(getApplicationContext()).load(temp_image).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView_condition);
            textView_condition.setText(condition.getText());

            recyclerView_extra_info.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            weatherInfoAdapter = new WeatherInfoAdapter(weatherInfoModelList, MainActivity.this);
            recyclerView_extra_info.setNestedScrollingEnabled(true);
            // recyclerView_extra_info.setHasFixedSize(true);
            recyclerView_extra_info.setAdapter(weatherInfoAdapter);

            recyclerView_forecast.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            dailyForecastAdapter = new DailyForecastAdapter(dailyForecastsList, MainActivity.this);
            recyclerView_forecast.setNestedScrollingEnabled(true);
            recyclerView_forecast.setAdapter(dailyForecastAdapter);
            if (linear_2_location.getVisibility() != View.VISIBLE) {
                linear_2_location.postDelayed(() -> fadeInAndShowLinearLayout(linear_2_location), 50);
                textView_date.postDelayed(() -> fadeInAndShowLinearLayout(textView_date), 100);
            }
            textView_last_update.postDelayed(() -> fadeInAndShowLinearLayout(textView_last_update), 150);
            relative_3_current_temp.postDelayed(() -> fadeInAndShowLinearLayout(relative_3_current_temp), 200);
            textView_7_day_forecast.postDelayed(() -> fadeInAndShowLinearLayout(textView_7_day_forecast), 250);
            recyclerView_forecast.postDelayed(() -> fadeInAndShowLinearLayout(recyclerView_forecast), 300);
            textView_additional_info.postDelayed(() -> fadeInAndShowLinearLayout(textView_additional_info), 350);
            recyclerView_extra_info.postDelayed(() -> fadeInAndShowLinearLayout(recyclerView_extra_info), 400);

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private List<DailyForecastModel> processForecastData(List<WeatherDataRealtime.Forecast.ForecastDay> forecastDays) {
        List<DailyForecastModel> dailyForecasts = new ArrayList<>();

        for (WeatherDataRealtime.Forecast.ForecastDay forecastDay : forecastDays) {
            String date = forecastDay.getDate();
            WeatherDataRealtime.Forecast.ForecastDay.Day day = forecastDay.getDay();

            double avgTempC = day.getAvgtemp_c();
            double minTempC = day.getMintemp_c();
            double maxTempC = day.getMaxtemp_c();
            int avgHumidity = day.getAvghumidity();
            String conditionText = day.getCondition().getText();
            String conditionIcon = day.getCondition().getIcon();

            DailyForecastModel dailyForecast = new DailyForecastModel(date, avgTempC, minTempC, maxTempC, avgHumidity, conditionText, conditionIcon);
            dailyForecasts.add(dailyForecast);
        }
        return dailyForecasts;
    }

    /*private void logDailyForecasts(List<DailyForecastModel> dailyForecasts) {
        for (DailyForecastModel dailyForecast : dailyForecasts) {
            Log.d("WeatherFetcher", "Date: " + dailyForecast.getDate());
            Log.d("WeatherFetcher", "Avg Temp: " + dailyForecast.getAvgTempC() + "°C");
            Log.d("WeatherFetcher", "Min Temp: " + dailyForecast.getMinTempC() + "°C");
            Log.d("WeatherFetcher", "Max Temp: " + dailyForecast.getMaxTempC() + "°C");
            Log.d("WeatherFetcher", "Avg Humidity: " + dailyForecast.getAvgHumidity() + "%");
            Log.d("WeatherFetcher", "Condition: " + dailyForecast.getConditionText());
            Log.d("WeatherFetcher", "Icon: " + dailyForecast.getConditionIcon());
        }
    }*/

    public static class AQIStatus {
        public static final int UNKNOWN = -1;
        public static final int GOOD_THRESHOLD = 50;
        public static final int SATISFACTORY_THRESHOLD = 100;
        public static final int MODERATE_THRESHOLD = 200;
        public static final int POOR_THRESHOLD = 300;
        public static final int VERY_POOR_THRESHOLD = 400;

        public static String getStatus(int aqi) {
            if (aqi == UNKNOWN) {
                return "UNAVAILABLE";
            } else if (aqi <= GOOD_THRESHOLD) {
                return "Good";
            } else if (aqi <= SATISFACTORY_THRESHOLD) {
                return "Satisfactory";
            } else if (aqi <= MODERATE_THRESHOLD) {
                return "Moderate";
            } else if (aqi <= POOR_THRESHOLD) {
                return "Poor";
            } else if (aqi <= VERY_POOR_THRESHOLD) {
                return "Very Poor";
            } else {
                return "Severe";
            }
        }
    }


    private void fadeOutAndHideLinearLayout(final View view) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        fadeOut.start();
    }

    private void fadeInAndShowLinearLayout(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.start();
    }

}