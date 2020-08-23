package com.example.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.Model.HourlyWeather;
import com.example.stormy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyRecycleAdapter extends RecyclerView.Adapter<HourlyRecycleAdapter.HourViewHolder> {
    private HourlyWeather[] mHourlyWeathers;
    private Context mContext;

    public HourlyRecycleAdapter(Context context, HourlyWeather[] hourlyWeathers) {
        mHourlyWeathers = hourlyWeathers;
        mContext = context;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        holder.bindView(mHourlyWeathers[position]);
    }

    @Override
    public int getItemCount() {
        return mHourlyWeathers.length;
    }


    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.timeLabel)
        TextView mTimeLabel;
        @BindView(R.id.iconImageView)
        ImageView mIconImageView;
        @BindView(R.id.summaryLabel)
        TextView mSummaryLabel;
        @BindView(R.id.temperatureLabel)
        TextView mTemperatureLabel;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindView(HourlyWeather hourlyWeather) {
            mTimeLabel.setText(hourlyWeather.getFormattedTime());
            mIconImageView.setImageResource(hourlyWeather.getIconId());
            mSummaryLabel.setText(hourlyWeather.getSummary());
            mTemperatureLabel.setText(hourlyWeather.getTemperatureAsInt() + "");
        }


        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s", time, temperature, summary);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}
