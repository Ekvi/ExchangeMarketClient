package com.ekvilan.exchangemarket.view.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.utils.JsonHelper;
import com.ekvilan.exchangemarket.utils.Validator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowAdsActivity extends ActionBarActivity {
    private TextView tvCity;
    private ImageView searchSettings;
    private CheckBox actionBuy, actionSale;
    private CheckBox usd, eur, rub;

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
                    JSONObject json = jsonHelper.createJson(city, actions, currencies);
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
}
