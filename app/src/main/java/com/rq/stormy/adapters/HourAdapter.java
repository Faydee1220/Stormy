package com.rq.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rq.stormy.R;
import com.rq.stormy.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Faydee on 2018/2/27.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] hours;
    private Context context;

    public HourAdapter(Context context, Hour[] hours) {
        this.context = context;
        this.hours = hours;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.timeTextView) TextView timeTextView;
        @BindView(R.id.summaryTextView) TextView summaryTextView;
        @BindView(R.id.temperatureTextView) TextView temperatureTextView;
        @BindView(R.id.iconImageView) ImageView iconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour) {
            timeTextView.setText(hour.getHour());
            summaryTextView.setText(hour.getSummary());
            temperatureTextView.setText(String.valueOf(hour.getTemperature()));
            iconImageView.setImageResource(hour.getIconId());
        }

        @Override
        public void onClick(View v) {
            String time = timeTextView.getText().toString();
            String temperature = temperatureTextView.getText().toString();
            String summary = summaryTextView.getText().toString();
            String message = String.format("At %s it wil be %s and %s",
                    time,
                    temperature,
                    summary);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
