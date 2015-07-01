package com.ekvilan.exchangemarket.view.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.ConnectionProvider;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.ekvilan.exchangemarket.view.DialogProvider;
import com.ekvilan.exchangemarket.view.adapters.AdvertisementAdapter;
import com.ekvilan.exchangemarket.view.listeners.RecyclerItemClickListener;

import java.util.List;


public class ShowMyAdsActivity extends Activity {
    private String LOG_TAG = "myLog";
    private final String SERVER_URL = "http://exchangemarket-ekvi.rhcloud.com/advertisement/getOwn";

    private RecyclerView recyclerView;
    private ImageView imageAddAds;

    private ConnectionProvider connectionProvider;
    private JsonHelper jsonHelper;
    private ActivityProvider activityProvider;
    private DialogProvider dialogProvider;

    private String jsonRequest;
    private List<Advertisement> ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_ads);

        connectionProvider = new ConnectionProvider();
        jsonHelper = new JsonHelper();
        activityProvider = new ActivityProvider();
        dialogProvider = new DialogProvider();

        initView();
        addListeners();

        jsonRequest = jsonHelper.createJson(getUserId()).toString();
        sendRequestToServer();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imageAddAds = (ImageView) findViewById(R.id.addAds);
    }

    private String getUserId() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return activityProvider.getUserId(accountManager);
    }

    private void addListeners() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(ads != null && ads.size() > 0) {
                            callAdvertisementActivity(position);
                        }
                    }
                })
        );

        imageAddAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddAdvertisementActivity();
            }
        });
    }

    private void callAdvertisementActivity(int position) {
        Intent intent = new Intent(this, AdvertisementActivity.class);
        intent.putExtra(getResources().getString(R.string.sendValueAdvertisement), ads.get(position));
        startActivityForResult(intent, 1);
    }

    private void callAddAdvertisementActivity() {
        Intent intent = new Intent(this, AddAdvertisementActivity.class);
        startActivityForResult(intent, 1);
    }

    private void sendRequestToServer() {
        if(!isConnected()){
            dialogProvider.createDialog(
                    getResources().getString(R.string.alertTitleInternetConnection),
                    getResources().getString(R.string.alertInternetConnectionMessage),
                    this,
                    getResources().getString(R.string.btnOk));
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
                String jsonFromServer = connectionProvider.getJson();

                if(jsonFromServer != null && !jsonFromServer.isEmpty()) {
                    fillActivityContent(jsonFromServer);
                }
            } else {
                Log.d(LOG_TAG, "get advertisements response - " + result);
            }
        }
    }

    private void fillActivityContent(String json) {
        List<Object> entities = jsonHelper.readJson(false, json);
        ads = activityProvider.transformToAdvertisements(entities);

        recyclerView.setAdapter(new AdvertisementAdapter(this, ads));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendRequestToServer();
    }
}
