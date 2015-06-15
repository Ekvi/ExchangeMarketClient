package com.ekvilan.exchangemarket.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.view.activities.AddAdvertisementActivity;


public class MainActivity extends Activity {
    private Button btnShow;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addButtonListeners();

    }

    private void initView() {
        btnShow = (Button)findViewById(R.id.btnShow);
        btnAdd = (Button) findViewById(R.id.btnAdd);
    }

    private void addButtonListeners() {
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callShowAdvertisementActivity();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddAdvertisementActivity();
            }
        });
    }

    private void callShowAdvertisementActivity() {
        Intent intent = new Intent(this, ShowAdsActivity.class);
        startActivityForResult(intent, 1);
    }

    private void callAddAdvertisementActivity() {
        Intent intent = new Intent(this, AddAdvertisementActivity.class);
        startActivityForResult(intent, 1);
    }

}
