package com.example.stormy.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.Model.CurrentWeather;
import com.example.stormy.Model.DailyWeather;
import com.example.stormy.Model.Forecast;
import com.example.stormy.Model.HourlyWeather;
import com.example.stormy.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;

    @BindView(R.id.timeLabel)
    TextView mTimeLabel;
    @BindView(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @BindView(R.id.humidityValue)
    TextView mHumidityValue;
    @BindView(R.id.precipValue)
    TextView mPrecipValue;
    @BindView(R.id.precipLabel)
    TextView mPrecipLabel;
    @BindView(R.id.summaryValue)
    TextView mSummaryValue;
    @BindView(R.id.locationLabel)
    TextView mLocationLabel;
    @BindView(R.id.iconImageView)
    ImageView mIconImageView;
    @BindView(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData(6.533932, 3.386154);
            }
        });

        fetchData(6.533932, 3.386154);
    }

    private void fetchData(double lat, double lon) {
        String apiKey = getString(R.string.apiKey);
        String forcastUrl = String.format("https://api.forecast.io/forecast/%1$s/%2$f,%3$f", apiKey, lat, lon);

        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forcastUrl).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleRefresh();
                            }
                        });
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mRefreshImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRefreshImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateDisplay() {
        CurrentWeather currentWeather = mForecast.getCurrentWeather();

        mTemperatureLabel.setText(currentWeather.getTemperature() + "");
        mTimeLabel.setText("At " + currentWeather.getFormattedTime() + " it will be");
        mHumidityValue.setText(currentWeather.getHumidity() + "");
        mPrecipValue.setText(currentWeather.getPrecipChance() + "%");
        mSummaryValue.setText(currentWeather.getSummary());
        mIconImageView.setImageDrawable(getResources().getDrawable(currentWeather.getIconId()));
        mLocationLabel.setText(currentWeather.getTimeZone());
        mPrecipLabel.setText(currentWeather.getPrecipType());
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrentWeather(getCurrentForecast(jsonData));
        forecast.setDailyWeathers(getDailyForecast(jsonData));
        forecast.setHourlyWeathers(getHourlyForecast(jsonData));

        return forecast;
    }

    private DailyWeather[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        DailyWeather[] dailyWeathers = new DailyWeather[data.length()];

        for (int index = 0; index < data.length(); index++) {
            DailyWeather dailyWeather = new DailyWeather();
            JSONObject dailyObject = data.getJSONObject(index);

            dailyWeather.setIcon(dailyObject.getString("icon"));
            dailyWeather.setSummary(dailyObject.getString("summary"));
            dailyWeather.setTemperatureMax(dailyObject.getDouble("temperatureMax"));
            dailyWeather.setTime(dailyObject.getLong("time"));
            dailyWeather.setTimezone(timezone);

            dailyWeathers[index] = dailyWeather;
        }

        return dailyWeathers;
    }

    private HourlyWeather[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        HourlyWeather[] hourlyWeathers = new HourlyWeather[data.length()];

        for (int index = 0; index < data.length(); index++) {
            HourlyWeather hourlyWeather = new HourlyWeather();
            JSONObject hourlyObject = data.getJSONObject(index);

            hourlyWeather.setTemperature(hourlyObject.getDouble("temperature"));
            hourlyWeather.setIcon(hourlyObject.getString("icon"));
            hourlyWeather.setSummary(hourlyObject.getString("summary"));
            hourlyWeather.setTime(hourlyObject.getLong("time"));
            hourlyWeather.setTimezone(timezone);

            hourlyWeathers[index] = hourlyWeather;
        }

        return hourlyWeathers;
    }

    private CurrentWeather getCurrentForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");


        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipType(currently.getString("precipType"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        currentWeather.getFormattedTime();
        currentWeather.getIconId();
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment(MainActivity.this);
        dialog.show(getSupportFragmentManager(), "");
    }
}