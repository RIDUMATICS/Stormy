package com.example.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stormy.Model.DailyWeather;
import com.example.stormy.R;

public class DailyAdapter extends BaseAdapter {
    DailyWeather[] mDailyWeathers;
    Context mContext;

    public DailyAdapter(Context context, DailyWeather[] dailyWeathers) {
        mDailyWeathers = dailyWeathers;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDailyWeathers.length;
    }

    @Override
    public Object getItem(int position) {
        return mDailyWeathers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            viewHolder.mIconImageView = convertView.findViewById(R.id.iconImageView);
            viewHolder.mIconTextView = convertView.findViewById(R.id.iconTextView);
            viewHolder.mDayTextView = convertView.findViewById(R.id.dayNameLabel);
            viewHolder.mTemperatureLabel = convertView.findViewById(R.id.temperatureLabel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DailyWeather dailyWeather = mDailyWeathers[position];

        viewHolder.mTemperatureLabel.setText(dailyWeather.getTemperatureMax() + "");
        viewHolder.mIconTextView.setText(dailyWeather.getIcon());
        viewHolder.mIconImageView.setImageResource(dailyWeather.getIconId());

        if (position == 0) {
            viewHolder.mDayTextView.setText("Today");
        } else {
            viewHolder.mDayTextView.setText(dailyWeather.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView mTemperatureLabel;
        ImageView mIconImageView;
        TextView mIconTextView;
        TextView mDayTextView;
    }
}
