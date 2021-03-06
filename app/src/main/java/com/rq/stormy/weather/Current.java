package com.rq.stormy.weather;

import com.rq.stormy.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Faydee on 2018/2/21.
 */
// Data Model
public class Current {
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipProbability;
    private String summary;
    private String timezone;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIcon() {
        return icon;
    }

    public int getIconId() {
        return Forecast.getIconId(icon);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        // 如果沒有設定時區，會自動使用 GMT +0
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        // 因為 Date 物件初始化是使用毫秒，1 秒 = 1000 毫秒，所以將時間秒數 * 1000
        Date date = new Date(time * 1000);
        String timeString = formatter.format(date);
        return timeString;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTemperature() {
        return (int) Math.round(temperature);
    }

    public String getFormattedTemperature() {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(temperature);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getPrecipProbability() {
        double percentage = precipProbability * 100;
        return (int) Math.round(percentage);
    }

    public void setPrecipProbability(double precipProbability) {
        this.precipProbability = precipProbability;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
