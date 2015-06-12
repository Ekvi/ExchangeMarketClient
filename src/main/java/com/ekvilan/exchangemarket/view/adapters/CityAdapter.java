package com.ekvilan.exchangemarket.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private LayoutInflater inflater;
    private String[] data;
    private String regionName;

    public CityAdapter(Context context, String[] data, String regionName) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.regionName = regionName;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CityViewHolder(inflater.inflate(R.layout.city_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.tvName.setText(data[position]);
        holder.tvRegion.setText(regionName);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRegion;

        public CityViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.city_activity_city);
            tvRegion = (TextView) itemView.findViewById(R.id.city_activity_region);
        }
    }
}
