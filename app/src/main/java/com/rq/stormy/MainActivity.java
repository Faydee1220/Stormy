package com.rq.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;

    @BindView(R.id.locationTextView) TextView locationTextView;
    @BindView(R.id.timeTextView) TextView timeTextView;
    @BindView(R.id.temperatureTextView) TextView temperatureTextView;
    @BindView(R.id.humidityValueTextView) TextView humidityValueTextView;
    @BindView(R.id.precipitationValueTextView) TextView precipitationValueTextView;
    @BindView(R.id.summaryTextView) TextView summaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 利用套件 ButterKnife 省略 findViewById
        ButterKnife.bind(this);

        String apiKey = "7dfca59c9f65002ef118da998903d236";
        double latitude = 25.088290;
        double longtitude = 121.478025;
        String forecastURL = "https://api.darksky.net/forecast/" + apiKey +
                "/" + latitude + "," + longtitude + "?units=si";

        if (isNetworkAvailable()) {
            // 使用 OkHttp 套件呼叫 API
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            // 使用非同步方式在背景執行緒呼叫 API
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        }
                        else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Main UI is running");

    }

    private void updateDisplay() {
        locationTextView.setText(currentWeather.getTimezone());
        timeTextView.setText(currentWeather.getFormattedTime());
        temperatureTextView.setText(currentWeather.getFormattedTemperature());
        humidityValueTextView.setText(String.valueOf(currentWeather.getHumidity()));
        precipitationValueTextView.setText(String.valueOf(currentWeather.getPrecipProbability()));
        summaryTextView.setText(currentWeather.getSummary());
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From JSON:" + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        String icon = currently.getString("icon");
        long time = currently.getLong("time");
        double temperature = currently.getDouble("temperature");
        double humidity = currently.getDouble("humidity");
        double precipProbability = currently.getDouble("precipProbability");
        String summary = currently.getString("summary");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setIcon(icon);
        currentWeather.setTime(time);
        currentWeather.setTemperature(temperature);
        currentWeather.setHumidity(humidity);
        currentWeather.setPrecipProbability(precipProbability);
        currentWeather.setSummary(summary);
        currentWeather.setTimezone(timezone);

        Log.d(TAG,currentWeather.getFormattedTime());

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
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
