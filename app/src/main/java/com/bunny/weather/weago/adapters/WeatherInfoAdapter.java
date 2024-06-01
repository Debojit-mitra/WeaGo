package com.bunny.weather.weago.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunny.weather.WeaGo.R;
import com.bunny.weather.weago.models.WeatherInfoModel;

import java.util.List;

public class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoAdapter.WeatherInfoViewHolder>{

    private final List<WeatherInfoModel> weatherInfoList;
    Context context;

    public WeatherInfoAdapter(List<WeatherInfoModel> weatherInfoList, Context context) {
        this.weatherInfoList = weatherInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherInfoAdapter.WeatherInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.bunny.weather.WeaGo.R.layout.item_weather_info, parent, false);
        return new WeatherInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherInfoAdapter.WeatherInfoViewHolder holder, int position) {

        int adapterPosition = holder.getAdapterPosition();

        Glide.with(context).load(weatherInfoList.get(adapterPosition).getIconResId()).into(holder.imageView_info);
        holder.textView_info_name.setText(weatherInfoList.get(adapterPosition).getLabel());
        holder.textView_info_data.setText(weatherInfoList.get(adapterPosition).getValue());

    }

    @Override
    public int getItemCount() {
        return weatherInfoList.size();
    }

    public class WeatherInfoViewHolder extends RecyclerView.ViewHolder {

        TextView textView_info_name, textView_info_data;
        ImageView imageView_info;

        public WeatherInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_info_name = itemView.findViewById(R.id.textView_info_name);
            textView_info_data = itemView.findViewById(R.id.textView_info_data);
            imageView_info = itemView.findViewById(R.id.imageView_info);
        }
    }
}
