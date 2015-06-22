package com.ekvilan.exchangemarket.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.DateUtils;

import java.util.List;


public class AdvertisementAdapter extends
        RecyclerView.Adapter<AdvertisementAdapter.AdvertisementViewHolder> {
    private LayoutInflater inflater;
    private List<Advertisement> ads;

    public AdvertisementAdapter(Context context, List<Advertisement> ads) {
        inflater = LayoutInflater.from(context);
        this.ads = ads;
    }

    @Override
    public AdvertisementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AdvertisementViewHolder(
                inflater.inflate(R.layout.advertisement_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(AdvertisementViewHolder adsViewHolder, int position) {
        adsViewHolder.date.setText(formatDate(ads.get(position).getDate()));
        adsViewHolder.action.setText(ads.get(position).getAction());
        adsViewHolder.currency.setText(ads.get(position).getCurrency());
        adsViewHolder.sum.setText(ads.get(position).getSum());
        adsViewHolder.rate.setText(ads.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class AdvertisementViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView action;
        TextView currency;
        TextView sum;
        TextView rate;

        public AdvertisementViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tvDate);
            action = (TextView) itemView.findViewById(R.id.tvAction);
            currency = (TextView) itemView.findViewById(R.id.tvCurrency);
            sum = (TextView) itemView.findViewById(R.id.tvSum);
            rate = (TextView) itemView.findViewById(R.id.tvRate);
        }
    }

    private String formatDate(String date) {
        DateUtils dateUtils = new DateUtils();
        return dateUtils.formatDate(date, dateUtils.createDate());
    }
}
