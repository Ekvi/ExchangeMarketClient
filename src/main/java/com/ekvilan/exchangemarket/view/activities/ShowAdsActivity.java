package com.ekvilan.exchangemarket.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.utils.Validator;
import com.ekvilan.exchangemarket.view.adapters.AdvertisementAdapter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowAdsActivity extends ActionBarActivity {
    private String LOG_TAG = "myLog";
    private final String SERVER_URL = "http://192.168.1.100:8080/advertisement/get";

    private TextView tvCity;
    private ImageView searchSettings;
    private CheckBox actionBuy, actionSale;
    private CheckBox usd, eur, rub;
    private RecyclerView recyclerView;
    private View header;

    private String json;
    private String jsonFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);

        initView();

        addListeners();
    }

    private void initView() {
        tvCity = (TextView) findViewById(R.id.tvCity);
        searchSettings = (ImageView) findViewById(R.id.searchSettings);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void addListeners() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAdsActivity.this, ShowEntitiesActivity.class);
                intent.putExtra(getResources().getString(R.string.tvCityContent),
                        getResources().getString(R.string.cityChoice));
                startActivityForResult(intent, 1);
            }
        });

        searchSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsWindow();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {return;}
        tvCity.setText(intent.getStringExtra(getResources().getString(R.string.city_name)));
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
                    JsonHelper jsonHelper = new JsonHelper();
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

    private void createDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(getResources().getString(R.string.btnOk), null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0], json);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase(getResources().getString(R.string.responseOk))) {
                if(jsonFromServer != null && !jsonFromServer.isEmpty()) {
                    fillActivityContent(jsonFromServer);
                }
            } else {
                Log.d(LOG_TAG, "get advertisements response - " + result);
            }
        }
    }

    private String POST(String urlString, String json){
        String result = "";

        DataOutputStream dataOutputStream;

        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            dataOutputStream = new DataOutputStream(connection.getOutputStream ());

            byte[] data = json.getBytes("UTF-8");

            dataOutputStream.write(data);
            dataOutputStream.flush ();
            dataOutputStream.close ();

            result = connection.getResponseMessage();
            jsonFromServer = convertInputStreamToString(connection.getInputStream());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Can't send json file!");
            e.printStackTrace();
        }
        return result;
    }

    private String convertInputStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    private void fillActivityContent(String json) {
        JsonHelper jsonHelper = new JsonHelper();
        List<Advertisement> ads = jsonHelper.readJson(json);

        recyclerView.setAdapter(new AdvertisementAdapter(this, ads));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
