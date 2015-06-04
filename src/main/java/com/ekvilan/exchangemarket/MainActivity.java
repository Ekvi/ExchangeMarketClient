package com.ekvilan.exchangemarket;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Button btnShow;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShow = (Button)findViewById(R.id.btnShow);
        btnAdd = (Button) findViewById(R.id.btnAdd);
    }

}
