package com.ekvilan.exchangemarket.view.activities;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.ConnectionProvider;
import com.ekvilan.exchangemarket.utils.DateUtils;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.utils.Validator;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.ekvilan.exchangemarket.view.DialogProvider;


public class AddAdvertisementActivity extends AppCompatActivity {
    private String LOG_TAG = "myLog";
    private final String SERVER_URL = "http://192.168.1.100:8080/advertisement/add";

    private Button btnAdd;
    private EditText etSum;
    private EditText etRate;
    private EditText etPhone;
    private EditText etArea;
    private EditText etComment;
    private RadioGroup radioGroupAction;
    private RadioGroup radioGroupCurrency;
    private TextView tvCity;

    private String actionUserChoice;
    private String currencyUserChoice;

    private Validator validator;
    private JsonHelper jsonHelper;
    private ActivityProvider activityProvider;
    private ConnectionProvider connectionProvider;
    private DialogProvider dialogProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advertisement);

        validator = new Validator();
        jsonHelper = new JsonHelper();
        activityProvider = new ActivityProvider();
        connectionProvider = new ConnectionProvider();
        dialogProvider = new DialogProvider();

        initView();
        setUpToolBar();
        setUpDefaultValues();
        addButtonListeners();
        addRadioGroupListeners();
        addTextViewListener();
    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btnAdd);
        etSum = (EditText)findViewById(R.id.etSum);
        etRate = (EditText)findViewById(R.id.etRate);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etArea = (EditText)findViewById(R.id.etArea);
        etComment = (EditText)findViewById(R.id.etComment);
        radioGroupAction = (RadioGroup) findViewById(R.id.action_group);
        radioGroupCurrency = (RadioGroup) findViewById(R.id.currency_group);
        tvCity = (TextView) findViewById(R.id.tvCity);
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void setUpDefaultValues() {
        actionUserChoice = getResources().getString(R.string.saleMessage);
        currencyUserChoice = getResources().getString(R.string.usdMessage);
    }

    private void addButtonListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {
                    if(!isConnected()){
                        createDialog(getResources().getString(R.string.alertTitleInternetConnection),
                                getResources().getString(R.string.alertInternetConnectionMessage));
                    } else {
                        new HttpAsyncTask().execute(SERVER_URL);
                    }
                }
            }
        });
    }

    private void addRadioGroupListeners() {
        radioGroupAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_sale:
                        actionUserChoice = getResources().getString(R.string.saleMessage);
                        break;
                    case R.id.radio_buy:
                        actionUserChoice = getResources().getString(R.string.buyMessage);
                        break;
                }
            }
        });

        radioGroupCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_usd:
                        currencyUserChoice = getResources().getString(R.string.usdMessage);
                        break;
                    case R.id.radio_eur:
                        currencyUserChoice = getResources().getString(R.string.eurMessage);
                        break;
                    case R.id.radio_rub:
                        currencyUserChoice = getResources().getString(R.string.rubMessage);
                        break;
                }
            }
        });
    }

    private void addTextViewListener() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAdvertisementActivity.this, ShowEntitiesActivity.class);
                intent.putExtra(getResources().getString(R.string.tvCityContent),
                        getResources().getString(R.string.cityChoice));
                startActivityForResult(intent, 1);
            }
        });
    }

    private boolean validateFields() {
        if (!validator.validateSum(etSum.getText().toString())) {
            createDialog(getResources().getString(R.string.alertTitleEmptyFields),
                    getResources().getString(R.string.alertSumMessage));
            return false;
        } else if(!validator.validateRate(etRate.getText().toString())){
            createDialog(getResources().getString(R.string.alertTitleEmptyFields),
                    getResources().getString(R.string.alertRateMessage));
            return false;
        } else if(validator.isEmptyField(etPhone.getText().toString())){
            createDialog(getResources().getString(R.string.alertTitleEmptyFields),
                    getResources().getString(R.string.alertEmptyFieldMessage));
            return false;
        } else if(validator.isEmptyCity(tvCity.getText().toString())){
            createDialog(getResources().getString(R.string.alertTitleEmptyFields),
                    getResources().getString(R.string.alertCityMessage));
            return false;
        } else {
            return true;
        }
    }

    private void createDialog(String title, String message) {
        dialogProvider.createDialog(title, message, this, getResources().getString(R.string.btnOk));
    }

    private Advertisement createAdvertisement() {
        return new Advertisement(getUserId(), tvCity.getText().toString(), actionUserChoice,
                currencyUserChoice, etSum.getText().toString(),etRate.getText().toString(),
                etPhone.getText().toString(), etArea.getText().toString(),
                etComment.getText().toString(), getDate());
    }

    private String getDate() {
        DateUtils dateUtils = new DateUtils();
        return dateUtils.createDate();
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
            String json = jsonHelper.createJson(createAdvertisement()).toString();
            return connectionProvider.POST(urls[0], json);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase(getResources().getString(R.string.responseOk))) {
                Toast.makeText(getBaseContext(),
                        getResources().getString(R.string.sendMessage), Toast.LENGTH_LONG).show();
            } else {
                Log.d(LOG_TAG, "add advertisement response - " + result);
            }
        }
    }

    private String getUserId() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return activityProvider.getUserId(accountManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {return;}
        tvCity.setText(intent.getStringExtra(getResources().getString(R.string.city_name)));
    }
}
