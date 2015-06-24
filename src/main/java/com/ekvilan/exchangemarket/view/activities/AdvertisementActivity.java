package com.ekvilan.exchangemarket.view.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.ConnectionProvider;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.ekvilan.exchangemarket.view.DialogProvider;


public class AdvertisementActivity extends AppCompatActivity {
    private final String SERVER_URL = "http://192.168.1.100:8080/advertisement/remove";
    private String LOG_TAG = "myLog";

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
    private Button btnDel;

    private ConnectionProvider connectionProvider;
    private JsonHelper jsonHelper;
    private DialogProvider dialogProvider;
    private ActivityProvider activityProvider;

    private Advertisement advertisement;
    private String jsonRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        connectionProvider = new ConnectionProvider();
        jsonHelper = new JsonHelper();
        dialogProvider = new DialogProvider();
        activityProvider = new ActivityProvider();

        advertisement = getIntent().getExtras().getParcelable("advertisement");

        initView();
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

        if(getUserId().equals(advertisement.getUserId())) {
            addDelButton();
            addListeners();
        }

        toolBarCity = (TextView) findViewById(R.id.city);
        toolBarAction = (TextView) findViewById(R.id.action);
        toolBarCurrency = (TextView) findViewById(R.id.currency);
    }

    private void addDelButton() {
        btnDel = new Button(this);
        btnDel.setText(getResources().getString(R.string.btnDel));

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            btnDel.setBackgroundDrawable(
                    ResourcesCompat.getDrawable(getResources(), R.drawable.button, null));
        }else{
            btnDel.setBackground(
                    ResourcesCompat.getDrawable(getResources(), R.drawable.button, null));
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.contentLayout);
        params.setMargins(0, 10, 0, 0);

        btnDel.setLayoutParams(params);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.advertisement_layout);
        container.addView(btnDel);
    }

    private void addListeners() {
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonRequest = jsonHelper.createJson(advertisement.getId()).toString();
                sendRequestToServer();
            }
        });
    }

    private String getUserId() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return activityProvider.getUserId(accountManager);
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
        toolBarCurrency.setText(getCurrencySign(advertisement.getCurrency()));
    }

    private String getCurrencySign(String currency) {
        if(currency.equalsIgnoreCase(getResources().getString(R.string.usdMessage))) {
            return getResources().getString(R.string.usdSign);
        } else if(currency.equalsIgnoreCase(getResources().getString(R.string.eurMessage))){
            return getResources().getString(R.string.euroSign);
        } else {
            return getResources().getString(R.string.rubSign);
        }
    }

    private void sendRequestToServer() {
        if(!isConnected()){
            createDialog(getResources().getString(R.string.alertTitleInternetConnection),
                    getResources().getString(R.string.alertInternetConnectionMessage));
        } else {
            new HttpAsyncTask().execute(SERVER_URL);
        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Activity.CONNECTIVITY_SERVICE);
        return connectionProvider.isConnected(connectivityManager);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return connectionProvider.POST(urls[0], jsonRequest);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase(getResources().getString(R.string.responseOk))) {
                Toast.makeText(getBaseContext(),
                        getResources().getString(R.string.deleteMessage), Toast.LENGTH_LONG).show();
            } else {
                Log.d(LOG_TAG, "add advertisement response - " + result);
            }
        }
    }

    private void createDialog(String title, String message) {
        dialogProvider.createDialog(title, message, this, getResources().getString(R.string.btnOk));
    }
}
