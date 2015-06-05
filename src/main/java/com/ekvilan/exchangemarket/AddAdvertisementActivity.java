package com.ekvilan.exchangemarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.utils.Validator;


public class AddAdvertisementActivity extends Activity {
    private Spinner city;
    private Button btnAdd;
    private EditText etSum;
    private EditText etRate;
    private EditText etPhone;
    private EditText etArea;
    private EditText etComment;
    /*private RadioButton radioSale;
    private RadioButton radioBuy;*/
    private RadioGroup radioGroupAction;
    private RadioGroup radioGroupCurrency;
    private Dialog incorrectSum;

    private String cityName;
    private String actionUserChoice;
    private String currencyUserChoice;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advertisement);

        validator = new Validator();

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
        /*radioSale = (RadioButton)findViewById(R.id.radio_sale);
        radioBuy = (RadioButton)findViewById(R.id.radio_buy);*/
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
                /*boolean b = validateFields();
                Log.d("my", "b= " + b);*/
                /*Log.d("my", "b= " + validateFields());*/
                if(validateFields()) {
                    Advertisement advertisement = createAdvertisement();
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
            createDialog(getResources().getString(R.string.alertSumMessage));
            return false;
        } else if(!validator.validateRate(etRate.getText().toString())){
            createDialog(getResources().getString(R.string.alertRateMessage));
            return false;
        } else if(validator.isEmptyField(etPhone.getText().toString())){
            createDialog(getResources().getString(R.string.alertEmptyFieldMessage));
            return false;
        } else {
            return true;
        }
    }

    private void createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.alertTitle));
        builder.setPositiveButton(getResources().getString(R.string.btnOk), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Advertisement createAdvertisement() {
        return new Advertisement(cityName, actionUserChoice, currencyUserChoice,
                etSum.getText().toString(),etRate.getText().toString(),
                etPhone.getText().toString(), etArea.getText().toString(),
                etComment.getText().toString());
    }
}