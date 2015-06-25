package com.ekvilan.exchangemarket.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ekvilan.exchangemarket.R;


public class MainActivity extends AppCompatActivity {
    private Button btnShow;
    private Button btnAdd;
    private Button myAds;
    private Button btnRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolBar();
        addButtonListeners();
    }

    private void initView() {
        btnShow = (Button)findViewById(R.id.btnShow);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        myAds = (Button) findViewById(R.id.myAds);
        btnRates = (Button) findViewById(R.id.btnRates);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void addButtonListeners() {
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(ShowAdsActivity.class);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(AddAdvertisementActivity.class);
            }
        });

        myAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(ShowMyAdsActivity.class);
            }
        });

        btnRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(RatesActivity.class);
            }
        });
    }

    private void callActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, 1);
    }
}
