package com.ekvilan.exchangemarket.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;


public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {
    private LayoutInflater inflater;
    private String[] data;

    public RegionAdapter(Context context, String[] data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RegionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RegionViewHolder(inflater.inflate(R.layout.region_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RegionViewHolder regionViewHolder, int position) {
        regionViewHolder.tvName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RegionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public RegionViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.region_activity_region);
        }
    }
}
