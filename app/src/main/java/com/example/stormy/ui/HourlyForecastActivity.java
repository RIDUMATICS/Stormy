package com.example.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.stormy.Model.HourlyWeather;
import com.example.stormy.R;
import com.example.stormy.adapters.HourlyRecycleAdapter;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity {

    HourlyWeather[] mHourlyWeathers;

    @BindView(R.id.hourlyRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(getString(R.string.hourly_forecast));

        mHourlyWeathers = Arrays.copyOf(parcelables, parcelables.length, HourlyWeather[].class);


        HourlyRecycleAdapter hourlyRecycleAdapter = new HourlyRecycleAdapter(this, mHourlyWeathers);

        String time = mHourlyWeathers[10].getFormattedTime();

        String time2 = mHourlyWeathers[20].getFormattedTime();

        mRecyclerView.setAdapter(hourlyRecycleAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

    }
}