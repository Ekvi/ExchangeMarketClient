package com.ekvilan.exchangemarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddAdvertisementActivity();
            }
        });
    }

    private void callAddAdvertisementActivity() {
        Intent intent = new Intent(this, AddAdvertisementActivity.class);
        startActivityForResult(intent, 1);
    }

}
