package com.ekvilan.exchangemarket.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.view.ActivityProvider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private Button btnShow;
    private Button btnAdd;
    private Button myAds;
    private Button btnRates;

    private InterstitialAd interstitialAd;
    private boolean isInterstitialShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadInterstitialAd();
        startScheduler();
        initView();
        initToolBar();
        addButtonListeners();

        ActivityProvider activityProvider = new ActivityProvider();
        activityProvider.showBanner(findViewById(R.id.adView));
    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAd_id));
        requestNewInterstitial();
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
                showInterstitialAd();
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

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                isInterstitialShow = true;
                callActivity(ShowAdsActivity.class);
            }
        });
    }

    private void callActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, 1);
    }

    private void showInterstitialAd() {
        if (interstitialAd.isLoaded() && !isInterstitialShow) {
            interstitialAd.show();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private void startScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                isInterstitialShow = false;
            }
        }, 0, 10, TimeUnit.MINUTES);
    }
}
