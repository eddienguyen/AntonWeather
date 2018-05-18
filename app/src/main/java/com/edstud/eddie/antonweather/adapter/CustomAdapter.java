package com.edstud.eddie.antonweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edstud.eddie.antonweather.R;
import com.edstud.eddie.antonweather.viewadapter.Detail;
import com.edstud.eddie.antonweather.viewadapter.Forecast;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Object> mObjects;

    public static final int DETAIL = 0;
    public static final int FORECAST = 1;

    public CustomAdapter(Context context, List<Object> objects){
        mContext = context;
        mObjects = objects;
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof Detail){
            return DETAIL;
        } else if (mObjects.get(position) instanceof Forecast){
            return FORECAST;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType){
            case DETAIL:
                View itemView0 = inflater.inflate(R.layout.row_detail_list, parent, false);
                return new DetailsViewHolder(itemView0);
            case FORECAST:
                View itemView1 = inflater.inflate(R.layout.row_forecast, parent, false);
                return new ForecastViewHolder(itemView1);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case DETAIL:
                Detail detail = (Detail) mObjects.get(position);
                DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;

                detailsViewHolder.windSpeed_data.setText(detail.getWindSpeed());
                detailsViewHolder.humidity_data.setText(detail.getHumidity());
                detailsViewHolder.pressure_data.setText(detail.getPressure());
                detailsViewHolder.visibility_data.setText(detail.getVisibility());
                detailsViewHolder.sunrise_data.setText(detail.getSunrise());
                detailsViewHolder.sunset_data.setText(detail.getSunset());
                break;
            case FORECAST:
                Forecast forecast = (Forecast) mObjects.get(position);
                ForecastViewHolder forecastViewHolder = (ForecastViewHolder) holder;
                forecastViewHolder.date.setText(forecast.getDay() + ", " + forecast.getDate());
                forecastViewHolder.desctiptionText.setText(forecast.getDescriptionText());
                forecastViewHolder.highTemp.setText(String.valueOf(forecast.getHighTemp()));
                forecastViewHolder.lowTemp.setText(String.valueOf(forecast.getLowTemp()));

//                int resourceId = getResources().getIdentifier("drawable/icon_" + code, null, getPackageName());
                int resourceId = mContext.getResources().getIdentifier("drawable/icon_" + forecast.getCode(), null, mContext.getPackageName());
                forecastViewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(resourceId));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView windSpeed_data, humidity_data, pressure_data, visibility_data, sunrise_data, sunset_data;

        public DetailsViewHolder(View itemView) {
            super(itemView);

            //TODO: view.FindView for icon
            windSpeed_data = itemView.findViewById(R.id.wind_speed_data);
            humidity_data = itemView.findViewById(R.id.humidity_data);
            pressure_data = itemView.findViewById(R.id.pressure_data);
            visibility_data = itemView.findViewById(R.id.visibility_data);
            sunrise_data = itemView.findViewById(R.id.sunrise_data);
            sunset_data = itemView.findViewById(R.id.sunset_data);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Detail detail = (Detail) mObjects.get(getAdapterPosition());
                    Toast.makeText(mContext, "Detail item clicked!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder{
        public TextView date, desctiptionText, highTemp, lowTemp;
        public ImageView imageView;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            date = itemView.findViewById(R.id.date);
            desctiptionText = itemView.findViewById(R.id.descriptionText);
            highTemp = itemView.findViewById(R.id.highTemp);
            lowTemp = itemView.findViewById(R.id.lowTemp);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Forecast forecast = (Forecast) mObjects.get(getAdapterPosition());
                    Toast.makeText(mContext, "Forecast item clicked!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
