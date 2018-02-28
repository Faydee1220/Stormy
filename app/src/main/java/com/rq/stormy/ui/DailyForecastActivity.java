package com.rq.stormy.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rq.stormy.R;
import com.rq.stormy.adapters.DayAdapter;
import com.rq.stormy.weather.Day;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyForecastActivity extends Activity {

    private Day[] days;
    @BindView(android.R.id.list) ListView listView;
    @BindView(android.R.id.empty) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

//        String[] daysOfTheWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,
//                daysOfTheWeek);
//        setListAdapter(adapter);

        Intent intent = getIntent();
        final Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        days = Arrays.copyOf(parcelables, parcelables.length, Day[].class);
        DayAdapter adapter = new DayAdapter(this, days);
        listView.setAdapter(adapter);
        listView.setEmptyView(textView);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = days[position].getDayOfTheWeek();
                String condition = days[position].getSummary();
                String temperatureMax = String.valueOf(days[position].getTemperatureMax());
                String message = String.format("On %s the high will be %s and it wll be %s",
                        dayOfTheWeek,
                        temperatureMax,
                        condition);
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
        listView.setOnItemClickListener(onItemClickListener);
    }



//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        String dayOfTheWeek = days[position].getDayOfTheWeek();
//        String condition = days[position].getSummary();
//        String temperatureMax = String.valueOf(days[position].getTemperatureMax());
//        String message = String.format("On %s the high will be %s and it wll be %s",
//                dayOfTheWeek,
//                temperatureMax,
//                condition);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//    }





}
