package com.rq.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rq.stormy.weather.Current;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private Current current;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.refreshImageView) ImageView refreshImageView;
    @BindView(R.id.iconImageView) ImageView iconImageView;
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

        apiGetForecast();

        Log.d(TAG, "Main UI is running");

    }

    private void apiGetForecast() {
        String apiKey = "7dfca59c9f65002ef118da998903d236";
        double latitude = 25.088290;
        double longtitude = 121.478025;
        String forecastURL = "https://api.darksky.net/forecast/" + apiKey +
                "/" + latitude + "," + longtitude + "?units=si";

        if (isNetworkAvailable()) {
            toggleRefresh();

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            current = getCurrentDetails(jsonData);
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
                    catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.refreshImageView) void refresh() {
        apiGetForecast();
    }

    private void updateDisplay() {
        locationTextView.setText(current.getTimezone());
        String time = "At " + current.getFormattedTime() + " it will be";
        timeTextView.setText(time);
        temperatureTextView.setText(String.valueOf(current.getTemperature()));
        humidityValueTextView.setText(String.valueOf(current.getHumidity()));
        String precipitation = String.valueOf(current.getPrecipProbability() + "%");
        precipitationValueTextView.setText(precipitation);
        summaryTextView.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        iconImageView.setImageDrawable(drawable);

    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
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

        Current current = new Current();
        current.setIcon(icon);
        current.setTime(time);
        current.setTemperature(temperature);
        current.setHumidity(humidity);
        current.setPrecipProbability(precipProbability);
        current.setSummary(summary);
        current.setTimezone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
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
