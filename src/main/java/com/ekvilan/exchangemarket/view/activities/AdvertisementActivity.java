package com.ekvilan.exchangemarket.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;


public class AdvertisementActivity extends AppCompatActivity {
    private TextView action;
    private TextView currency;
    private TextView sum;
    private TextView rate;
    private TextView phone;
    private TextView area;
    private TextView comment;
    private TextView toolBarCity;
    private TextView toolBarAction;
    private TextView toolBarCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        initView();

        Advertisement advertisement = getIntent().getExtras().getParcelable("advertisement");

        setUpContent(advertisement);
        setUpToolBar(advertisement);
    }

    private void initView() {
        action = (TextView) findViewById(R.id.tvAction);
        currency = (TextView) findViewById(R.id.tvCurrency);
        sum = (TextView) findViewById(R.id.tvSum);
        rate = (TextView) findViewById(R.id.tvRate);
        phone = (TextView) findViewById(R.id.tvPhone);
        area = (TextView) findViewById(R.id.tvArea);
        comment = (TextView) findViewById(R.id.tvComment);

        toolBarCity = (TextView) findViewById(R.id.city);
        toolBarAction = (TextView) findViewById(R.id.action);
        toolBarCurrency = (TextView) findViewById(R.id.currency);
    }

    private void setUpContent(Advertisement advertisement) {
        if(advertisement.getAction().equalsIgnoreCase(
                getResources().getString(R.string.saleMessage))) {
            action.setText(getResources().getString(R.string.adsSaleMessage));
        } else {
            action.setText(getResources().getString(R.string.adsBuyMessage));
        }

        sum.setText(advertisement.getSum());
        if(advertisement.getCurrency().equalsIgnoreCase(
                getResources().getString(R.string.usdMessage))) {
            currency.setText(getResources().getString(R.string.adsUsdMessage));
        } else if(advertisement.getCurrency().equalsIgnoreCase(
                getResources().getString(R.string.rubMessage))){
            currency.setText(getResources().getString(R.string.adsRubMessage));
        } else {
            currency.setText(getResources().getString(R.string.eurMessage));
        }

        rate.setText("Курс " + advertisement.getRate());
        phone.setText("Телефон для связи: " + advertisement.getPhone());
        String cityArea = advertisement.getArea().isEmpty() ? "не указан" : advertisement.getArea();
        area.setText("Район города: " + cityArea);
        comment.setText(advertisement.getComment());
    }

    private void setUpToolBar(Advertisement advertisement) {
        toolBarCity.setText(advertisement.getCity());
        toolBarAction.setText(advertisement.getAction());
        toolBarCurrency.setText(advertisement.getCurrency());
    }
}
