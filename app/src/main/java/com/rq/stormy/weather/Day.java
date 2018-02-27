package com.rq.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Faydee on 2018/2/26.
 */

public class Day implements Parcelable {
    public static final String TAG = Day.class.getSimpleName();
    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;
    private String timezone;

    public Day() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTemperatureMax() {
        int temperature = (int) Math.round(temperatureMax);
        Log.d(TAG, String.valueOf(temperature));
        return temperature;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getIconId() {
        return Forecast.getIconId(icon);
    }

    public String getDayOfTheWeek() {
        // EEEE 代表星期幾
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // dest 代表 destination
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(temperatureMax);
        dest.writeString(icon);
        dest.writeString(timezone);
    }

    protected Day(Parcel in) {
        // 要和 writeToParcel 的順序相同
        time = in.readLong();
        summary = in.readString();
        temperatureMax = in.readDouble();
        icon = in.readString();
        timezone = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
