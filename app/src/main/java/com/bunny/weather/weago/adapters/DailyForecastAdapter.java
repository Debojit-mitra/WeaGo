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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunny.weather.WeaGo.R;
import com.bunny.weather.weago.models.DailyForecastModel;
import com.bunny.weather.weago.utils.Extras;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder> {

    private final List<DailyForecastModel> forecastList;
    Context context;

    public DailyForecastAdapter(List<DailyForecastModel> forecastList, Context context) {
        this.forecastList = forecastList;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyForecastAdapter.DailyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast_info, parent, false);
        return new DailyForecastAdapter.DailyForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastAdapter.DailyForecastViewHolder holder, int position) {

        int adapterPosition = holder.getAdapterPosition();

        String condition_img_url = "https:" + forecastList.get(adapterPosition).getConditionIcon();
        if (condition_img_url.contains("64x64")){
            condition_img_url = condition_img_url.replace("64x64", "128x128");
        }
        Glide.with(context).load(condition_img_url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageView_condition);
        String day_date = forecastList.get(adapterPosition).getDate();
        day_date = Extras.formatDateStringType2(day_date);
        holder.textView_info_day_date.setText(day_date);
        String mix_min_temp = forecastList.get(adapterPosition).getMaxTempC() + "°/" +  forecastList.get(adapterPosition).getMinTempC() + "°";
        holder.textView_temp_min_max.setText(mix_min_temp);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class DailyForecastViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_condition;
        TextView textView_info_day_date, textView_temp_min_max;
        public DailyForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_info_day_date = itemView.findViewById(R.id.textView_info_day_date);
            textView_temp_min_max = itemView.findViewById(R.id.textView_temp_min_max);
            imageView_condition = itemView.findViewById(R.id.imageView_condition);
        }
    }
}
