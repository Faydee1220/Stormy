package com.rq.stormy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rq.stormy.R;
import com.rq.stormy.weather.Day;

/**
 * Created by Faydee on 2018/2/27.
 */

public class DayAdapter extends BaseAdapter {

    private Context context;
    private Day[] days;

    public DayAdapter(Context context, Day[] days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    // Tag items for easy reference, we aren't going to use this
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 類似 iOS tableView 的 cellForRow
    // convertView 是可以 reuse 的，第一次 getView 時會是 null
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // brand new

            // The LayoutInflater takes your layout XML-files and creates different View-objects from its contents

            // The adapters are built to reuse Views, when a View is scrolled so that is no longer visible,
            // it can be used for one of the new Views appearing.
            // This reused View is the convertView.
            // If this is null it means that there is no recycled View and we have to create a new one,
            // otherwise we should use it to avoid creating a new.

            // view group root 沒用到時放 null
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.circleImageView = convertView.findViewById(R.id.circleImageView);
            holder.iconImageView = convertView.findViewById(R.id.iconImageView);
            holder.temperatureTextView = convertView.findViewById(R.id.temperatureTextView);
            holder.dayNameTextView = convertView.findViewById(R.id.dayNameTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = days[position];
        holder.circleImageView.setImageResource(R.drawable.bg_temperature);
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureTextView.setText(String.valueOf(day.getTemperatureMax()));
        if (position == 0) {
            holder.dayNameTextView.setText("Today");
        }
        else {
            holder.dayNameTextView.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView circleImageView;
        ImageView iconImageView; // public by default
        TextView temperatureTextView;
        TextView dayNameTextView;
    }



}
