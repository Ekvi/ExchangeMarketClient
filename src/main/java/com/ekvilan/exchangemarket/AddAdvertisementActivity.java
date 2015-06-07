package com.ekvilan.exchangemarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public class AddAdvertisementActivity extends Activity {
    private String LOG_TAG = "myLog";
    private final String SERVER_URL = "http://192.168.1.100:8080/advertisement/add";

    private Spinner city;
    private Button btnAdd;
    private EditText etSum;
    private EditText etRate;
    private EditText etPhone;
    private EditText etArea;
    private EditText etComment;
    private RadioGroup radioGroupAction;
    private RadioGroup radioGroupCurrency;

    private String cityName;
    private String actionUserChoice;
    private String currencyUserChoice;

    private Validator validator;
    private JsonHelper jsonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advertisement);

        validator = new Validator();
        jsonHelper = new JsonHelper();

        initView();
        setUpListCities();
        addButtonListeners();
        addRadioGroupListeners();
    }

    private void initView() {
        city = (Spinner) findViewById(R.id.spinnerCity);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        etSum = (EditText)findViewById(R.id.etSum);
        etRate = (EditText)findViewById(R.id.etRate);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etArea = (EditText)findViewById(R.id.etArea);
        etComment = (EditText)findViewById(R.id.etComment);
        radioGroupAction = (RadioGroup) findViewById(R.id.action_group);
        radioGroupCurrency = (RadioGroup) findViewById(R.id.currency_group);
    }

    private void setUpListCities() {
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
        } else {
            return true;
        }
    }

    private void createDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(getResources().getString(R.string.btnOk), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Advertisement createAdvertisement() {
        return new Advertisement("test@gmail.com", cityName, actionUserChoice, currencyUserChoice,
                etSum.getText().toString(),etRate.getText().toString(),
                etPhone.getText().toString(), etArea.getText().toString(),
                etComment.getText().toString(), new Date().toString());
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
            return POST(urls[0], createAdvertisement());
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

    private String POST(String urlString, Advertisement advertisement){
        String result = "";
        String json = jsonHelper.createJson(advertisement).toString();
        
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
        } catch (IOException e) {
            Log.d(LOG_TAG, "Can't send json file!");
            e.printStackTrace();
        }

        return result;
    }
}
