package com.ekvilan.exchangemarket.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.ConnectionProvider;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.utils.Validator;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.ekvilan.exchangemarket.view.DialogProvider;
import com.ekvilan.exchangemarket.view.adapters.AdvertisementAdapter;
import com.ekvilan.exchangemarket.view.listeners.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class ShowAdsActivity extends AppCompatActivity {
    private String LOG_TAG = "myLog";
    private final String SERVER_URL = "http://exchangemarket-ekvi.rhcloud.com/advertisement/get";

    private TextView tvCity;
    private ImageView searchSettings;
    private CheckBox actionBuy, actionSale;
    private CheckBox usd, eur, rub;
    private ImageView imageAddAds;
    private RecyclerView recyclerView;

    private JsonHelper jsonHelper;
    private ConnectionProvider connectionProvider;
    private DialogProvider dialogProvider;
    private ActivityProvider activityProvider;

    private String json;
    private List<Advertisement> ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);

        jsonHelper = new JsonHelper();
        connectionProvider = new ConnectionProvider();
        dialogProvider = new DialogProvider();
        activityProvider = new ActivityProvider();

        initView();
        addListeners();

        fillActivityContent();
    }

    private void initView() {
        tvCity = (TextView) findViewById(R.id.tvCity);
        searchSettings = (ImageView) findViewById(R.id.searchSettings);
        imageAddAds = (ImageView) findViewById(R.id.addAds);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void addListeners() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callShowEntitiesActivity();
            }
        });

        searchSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsWindow();
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(ads != null && ads.size() > 0) {
                            callAdvertisementActivity(ads.get(position));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {return;}
        tvCity.setText(intent.getStringExtra(getResources().getString(R.string.city_name)));

        String className = intent.getStringExtra(getResources().getString(R.string.sendValueClassName));
        if (className != null) {
            if(className.equals(getResources().getString(R.string.nameShowAdsActivity))) {
                json = intent.getStringExtra(getResources().getString(R.string.sendValueJson));
                sendRequestToServer();
            }
        }
    }

    private void showSettingsWindow() {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.search_settings, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initCheckBoxes(popupView);

        popupWindow.showAsDropDown(searchSettings);

        Button btnOk = (Button)popupView.findViewById(R.id.btnSettingsOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validator validator = new Validator();
                List<String> actions = getActions();
                List<String> currencies = getCurrencies();
                String city = tvCity.getText().toString();

                popupWindow.dismiss();

                if(validator.isEmptyCity(city)) {
                    createDialog(getResources().getString(R.string.alertTitleEmptyFields),
                            getResources().getString(R.string.alertCityMessage));
                } else {
                    json = jsonHelper.createJson(city, actions, currencies).toString();
                    sendRequestToServer();
                }
            }
        });
    }

    private void initCheckBoxes(View popupView) {
        actionBuy = (CheckBox) popupView.findViewById(R.id.checkBoxBuy);
        actionSale = (CheckBox) popupView.findViewById(R.id.checkBoxSale);
        usd = (CheckBox) popupView.findViewById(R.id.checkBoxUsd);
        eur = (CheckBox) popupView.findViewById(R.id.checkBoxEur);
        rub = (CheckBox) popupView.findViewById(R.id.checkBoxRub);
    }

    private List<String> getActions() {
        List<String> actions = new ArrayList<String>();

        if(actionBuy.isChecked()) {
            actions.add(getResources().getString(R.string.buyMessage));
        }

        if(actionSale.isChecked()) {
            actions.add(getResources().getString(R.string.saleMessage));
        }
        return actions;
    }

    private List<String> getCurrencies() {
        List<String> currencies = new ArrayList<String>();

        if(usd.isChecked()) {
            currencies.add(getResources().getString(R.string.usdMessage));
        }

        if(eur.isChecked()) {
            currencies.add(getResources().getString(R.string.eurMessage));
        }

        if(rub.isChecked()) {
            currencies.add(getResources().getString(R.string.rubMessage));
        }
        return currencies;
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
            return connectionProvider.POST(urls[0], json);
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

    private void fillActivityContent() {
        List<Advertisement> ads = new ArrayList<>();

        recyclerView.setAdapter(new AdvertisementAdapter(this, ads));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillActivityContent(String json) {
        List<Object> entities = jsonHelper.readJson(false, json);
        ads = activityProvider.transformToAdvertisements(entities);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdvertisementAdapter(this, ads));
    }

    private void callShowEntitiesActivity() {
        Intent intent = new Intent(this, ShowEntitiesActivity.class);
        intent.putExtra(getResources().getString(R.string.tvCityContent),
                getResources().getString(R.string.cityChoice));
        startActivityForResult(intent, 1);
    }

    private void callAddAdvertisementActivity() {
        Intent intent = new Intent(this, AddAdvertisementActivity.class);
        startActivity(intent);
    }

    private void callAdvertisementActivity(Advertisement advertisement) {
        Intent intent = new Intent(this, AdvertisementActivity.class);
        intent.putExtra(getResources().getString(R.string.sendValueAdvertisement), advertisement);
        intent.putExtra(getResources().getString(R.string.sendValueJson), json);
        intent.putExtra(getResources().getString(R.string.sendValueClassName),
                getResources().getString(R.string.nameShowAdsActivity));
        startActivityForResult(intent, 1);
    }

    private void createDialog(String title, String message) {
        dialogProvider.createDialog(title, message, this, getResources().getString(R.string.btnOk));
    }
}
