package com.ekvilan.exchangemarket.view.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.ekvilan.exchangemarket.view.Urls;


public class AdvertisementActivity extends AppCompatActivity {
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
    private Button btnMakeCall;

    private ConnectionProvider connectionProvider;
    private JsonHelper jsonHelper;
    private DialogProvider dialogProvider;
    private ActivityProvider activityProvider;

    private Advertisement advertisement;
    private String jsonRequest;
    private String savedRequestJson;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        connectionProvider = new ConnectionProvider();
        jsonHelper = new JsonHelper();
        dialogProvider = new DialogProvider();
        activityProvider = new ActivityProvider();

        getExtraValues();
        initView();
        addListeners();
        setUpContent(advertisement);
        setUpToolBar(advertisement);
    }

    private void getExtraValues() {
        advertisement = getIntent()
                .getExtras()
                .getParcelable(getResources().getString(R.string.sendValueAdvertisement));
        savedRequestJson = getIntent()
                .getStringExtra(getResources().getString(R.string.sendValueJson));
        className = getIntent()
                .getStringExtra(getResources().getString(R.string.sendValueClassName));
    }

    private void initView() {
        action = (TextView) findViewById(R.id.tvAction);
        currency = (TextView) findViewById(R.id.tvCurrency);
        sum = (TextView) findViewById(R.id.tvSum);
        rate = (TextView) findViewById(R.id.tvRate);
        phone = (TextView) findViewById(R.id.tvPhone);
        area = (TextView) findViewById(R.id.tvArea);
        comment = (TextView) findViewById(R.id.tvComment);
        btnMakeCall = (Button) findViewById(R.id.btnMakeCall);

        if(getUserId().equals(advertisement.getUserId())) {
            addDelButton();
            addButtonDelListener();
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
        params.setMargins(0, 20, 0, 0);

        btnDel.setLayoutParams(params);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.advertisement_layout);
        container.addView(btnDel);
    }

    private void addListeners() {
        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(advertisement.getPhone());
            }
        });
    }

    private void addButtonDelListener() {
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
        setActionTextView(advertisement);
        setCurrencyTextView(advertisement);

        String cityArea = advertisement.getArea().isEmpty() ? "не указан" : advertisement.getArea();
        area.setText(cityArea);
        sum.setText(advertisement.getSum());
        rate.setText(advertisement.getRate());
        phone.setText(advertisement.getPhone());
        comment.setText(advertisement.getComment());
    }

    private void setActionTextView(Advertisement advertisement) {
        if(advertisement.getAction().equalsIgnoreCase(
                getResources().getString(R.string.saleMessage))) {
            action.setText(getResources().getString(R.string.adsSaleMessage));
        } else {
            action.setText(getResources().getString(R.string.adsBuyMessage));
        }
    }

    private void setCurrencyTextView(Advertisement advertisement) {
        if(advertisement.getCurrency().equalsIgnoreCase(
                getResources().getString(R.string.usdMessage))) {
            currency.setText(getResources().getString(R.string.adsUsdMessage));
        } else if(advertisement.getCurrency().equalsIgnoreCase(
                getResources().getString(R.string.rubMessage))){
            currency.setText(getResources().getString(R.string.adsRubMessage));
        } else {
            currency.setText(getResources().getString(R.string.eurMessage));
        }
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
            new HttpAsyncTask(
                    dialogProvider.createProgressDialog(
                            this, getResources().getString(R.string.removingMessage)))
                    .execute(Urls.ADVERTISEMENT_REMOVE.getValue());
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
            return connectionProvider.POST(urls[0], jsonRequest);
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if(result.equalsIgnoreCase(getResources().getString(R.string.responseOk))) {
                Toast.makeText(getBaseContext(),
                        getResources().getString(R.string.deleteMessage), Toast.LENGTH_LONG).show();
                returnToPreviousActivity(className, savedRequestJson);
            } else {
                Log.d(LOG_TAG, "add advertisement response - " + result);
            }
        }
    }

    private void createDialog(String title, String message) {
        dialogProvider.createDialog(title, message, this, getResources().getString(R.string.btnOk));
    }

    private void returnToPreviousActivity(String className, String json) {
        String name = getResources().getString(R.string.nameShowAdsActivity);
        if(className != null && className.equals(name)) {
            setResult(RESULT_OK, createIntent(json));
            finish();
        } else {
            this.finish();
        }
    }

    private Intent createIntent(String json) {
        Intent intent = new Intent(this, ShowAdsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(getResources().getString(R.string.sendValueJson), json);
        intent.putExtra(getResources().getString(R.string.sendValueClassName), className);
        intent.putExtra(getResources().getString(R.string.city_name), advertisement.getCity());

        return intent;
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
