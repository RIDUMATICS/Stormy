package com.example.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.Model.DailyWeather;
import com.example.stormy.R;
import com.example.stormy.adapters.DailyAdapter;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyForecastActivity extends AppCompatActivity {
    DailyWeather[] mDailyWeathers;
    @BindView(android.R.id.list)
    ListView mListView;
    @BindView(android.R.id.empty)
    TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(getString(R.string.daily_forecast));
        mDailyWeathers = Arrays.copyOf(parcelables, parcelables.length, DailyWeather[].class);

        DailyAdapter dailyAdapter = new DailyAdapter(this, mDailyWeathers);
        mListView.setAdapter(dailyAdapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyWeather dailyWeather = mDailyWeathers[position];
                String dayOfTheWeek = dailyWeather.getDayOfTheWeek();
                String conditions = dailyWeather.getSummary();
                String highTemp = String.valueOf(dailyWeather.getTemperatureMax());

                String msg = String.format("On %s the high will be %s and it will be %s ", dayOfTheWeek, highTemp, conditions);
                Toast.makeText(DailyForecastActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}