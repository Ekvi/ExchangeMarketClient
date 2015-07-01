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


public class AdvertisementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int EMPTY_VIEW = 10;

    private LayoutInflater inflater;
    private List<Advertisement> ads;

    public AdvertisementAdapter(Context context, List<Advertisement> ads) {
        inflater = LayoutInflater.from(context);
        this.ads = ads;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(inflater.inflate(R.layout.empty_row, parent, false));
        }

        return new AdvertisementViewHolder(
                inflater.inflate(R.layout.advertisement_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof AdvertisementViewHolder) {
            AdvertisementViewHolder adsHolder = (AdvertisementViewHolder) viewHolder;

            adsHolder.date.setText(formatDate(ads.get(position).getDate()));
            adsHolder.action.setText(ads.get(position).getAction());
            adsHolder.currency.setText(ads.get(position).getCurrency());
            adsHolder.sum.setText(ads.get(position).getSum());
            adsHolder.rate.setText(ads.get(position).getRate());
        }
    }

    @Override
    public int getItemCount() {
        return ads.size() > 0 ? ads.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (ads.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
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

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String formatDate(String date) {
        DateUtils dateUtils = new DateUtils();
        return dateUtils.formatDate(date, dateUtils.createDate());
    }
}
