package com.rq.stormy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rq.stormy.R;
import com.rq.stormy.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Faydee on 2018/2/27.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] hours;

    public HourAdapter(Hour[] hours) {
        this.hours = hours;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timeTextView) TextView timeTextView;
        @BindView(R.id.summaryTextView) TextView summaryTextView;
        @BindView(R.id.temperatureTextView) TextView temperatureTextView;
        @BindView(R.id.iconImageView) ImageView iconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHour(Hour hour) {
            timeTextView.setText(hour.getHour());
            summaryTextView.setText(hour.getSummary());
            temperatureTextView.setText(String.valueOf(hour.getTemperature()));
            iconImageView.setImageResource(hour.getIconId());
        }
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHoler = new HourViewHolder(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(hours[position]);
    }

    @Override
    public int getItemCount() {
        return hours.length;
    }


}
