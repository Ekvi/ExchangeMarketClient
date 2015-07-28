package com.ekvilan.exchangemarket.view.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Rates;
import com.ekvilan.exchangemarket.utils.ConnectionProvider;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.ekvilan.exchangemarket.view.DialogProvider;
import com.ekvilan.exchangemarket.view.Urls;

import java.util.List;

public class RatesActivity extends AppCompatActivity {
    private String LOG_TAG = "myLog";

    private TextView tvUsd;
    private TextView tvEur;
    private TextView tvRub;
    private TextView banksBuy;
    private TextView banksSale;
    private TextView marketBuy;
    private TextView marketSale;
    private TextView nbuBuy;
    private Drawable dark;
    private Drawable light;

    private DialogProvider dialogProvider;
    private ConnectionProvider connectionProvider;
    private JsonHelper jsonHelper;
    private ActivityProvider activityProvider;

    private List<Rates> ratesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        dialogProvider = new DialogProvider();
        connectionProvider = new ConnectionProvider();
        jsonHelper = new JsonHelper();
        activityProvider = new ActivityProvider();

        initToolBar();
        initView();
        sendRequestToServer();
        addListeners();

        activityProvider.showBanner(findViewById(R.id.adView));
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.rates_tool_bar);
        setSupportActionBar(toolbar);
    }

    private void initView() {
        dark = ResourcesCompat.getDrawable(getResources(), R.drawable.currency_off, null);
        light = ResourcesCompat.getDrawable(getResources(), R.drawable.currency_on, null);

        tvUsd = (TextView) findViewById(R.id.tvUsd);
        tvEur = (TextView) findViewById(R.id.tvEur);
        tvRub = (TextView) findViewById(R.id.tvRub);

        banksBuy = (TextView) findViewById(R.id.banks_buy);
        banksSale = (TextView) findViewById(R.id.banks_sale);
        marketBuy = (TextView) findViewById(R.id.market_buy);
        marketSale = (TextView) findViewById(R.id.market_sale);
        nbuBuy = (TextView) findViewById(R.id.nbu_buy);
    }

    private void addListeners() {
        tvUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUsdRates();
            }
        });

        tvEur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEurRates();
            }
        });

        tvRub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRubRates();
            }
        });
    }

    private void sendRequestToServer() {
        if(!isConnected()){
            dialogProvider.createDialog(
                    getResources().getString(R.string.alertTitleInternetConnection),
                    getResources().getString(R.string.alertInternetConnectionMessage),
                    this,
                    getResources().getString(R.string.btnOk));
        } else {
            new HttpAsyncTask(
                    dialogProvider.createProgressDialog(
                            this, getResources().getString(R.string.loadingMessage)))
                    .execute(Urls.RATES_GET.getValue());
        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Activity.CONNECTIVITY_SERVICE);
        return connectionProvider.isConnected(connectivityManager);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        private HttpAsyncTask(ProgressDialog progress){
            this.progress = progress;
        }

        @Override
        protected void onPreExecute() {
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return connectionProvider.POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();

            if(result.equalsIgnoreCase(getResources().getString(R.string.responseOk))) {
                String jsonFromServer = connectionProvider.getJson();
                if(jsonFromServer != null && !jsonFromServer.isEmpty()) {
                    fillActivityContent(jsonFromServer);
                }
            } else {
                Log.d(LOG_TAG, "get rates response - " + result);
                Toast.makeText(
                        RatesActivity.this,
                        getResources().getString(R.string.emptyRatesMessage),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fillActivityContent(String json) {
        List<Object> entities = jsonHelper.readJson(true, json);
        ratesList = activityProvider.transformToRates(entities);

        setUsdRates();
    }

    private void setUsdRates() {
        setRatesBackgroundInToolBar(tvUsd, tvEur, tvRub, light, dark, dark);

        setRates(ratesList.get(0).getUsdBuy(),
                ratesList.get(0).getUsdSale(), ratesList.get(1).getUsdBuy(),
                ratesList.get(1).getUsdSale(), ratesList.get(2).getUsdBuy());
    }

    private void setEurRates() {
        setRatesBackgroundInToolBar(tvUsd, tvEur, tvRub, dark, light, dark);

        setRates(ratesList.get(0).getEurBuy(),
                ratesList.get(0).getEurSale(), ratesList.get(1).getEurBuy(),
                ratesList.get(1).getEurSale(), ratesList.get(2).getEurBuy());
    }

    private void setRubRates() {
        setRatesBackgroundInToolBar(tvUsd, tvEur, tvRub, dark, dark, light);

        setRates(ratesList.get(0).getRubBuy(),
                ratesList.get(0).getRubSale(), ratesList.get(1).getRubBuy(),
                ratesList.get(1).getRubSale(), ratesList.get(2).getRubBuy());
    }

    private void setRatesBackgroundInToolBar(View tvUsd, View tvEur, View tvRub,
                                             Drawable dUsd, Drawable dEur, Drawable dRub) {
        setBackground(tvUsd, dUsd);
        setBackground(tvEur, dEur);
        setBackground(tvRub, dRub);
    }

    private void setBackground(View view, Drawable drawable) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            view.setBackgroundDrawable(drawable);
        }else{
            view.setBackground(drawable);
        }
    }

    private void setRates(String textBanksBuy, String textBanksSale,
                          String textMarketBuy, String textMarketSale, String textNbuBuy) {

        banksBuy.setText(textBanksBuy);
        banksSale.setText(textBanksSale);
        marketBuy.setText(textMarketBuy);
        marketSale.setText(textMarketSale);
        nbuBuy.setText(textNbuBuy);
    }
}



