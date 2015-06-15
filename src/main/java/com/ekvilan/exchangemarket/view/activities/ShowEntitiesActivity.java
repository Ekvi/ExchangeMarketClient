package com.ekvilan.exchangemarket.view.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.view.adapters.CityAdapter;
import com.ekvilan.exchangemarket.view.adapters.RegionAdapter;
import com.ekvilan.exchangemarket.view.listeners.RecyclerItemClickListener;

public class ShowEntitiesActivity extends ActionBarActivity {
    public static final String CHOICE_CITY_MESSAGE = "Выберите город";

    private RecyclerView recyclerView;
    private TextView tvCity;

    private String cityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entities);

        Intent intent = getIntent();
        cityTextView = intent.getStringExtra(getResources().getString(R.string.tvCityContent));

        initView();
        initToolBar();
        addListener();

        fillActivityContent();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvCity = (TextView) findViewById(R.id.tvCity);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void fillActivityContent() {
        String[] values = getResources().getStringArray(R.array.ukrainian_regions);

        recyclerView.setAdapter(new RegionAdapter(this, values));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillActivityContent(String region, int position) {
        int resourceId = getResources().getIdentifier("cities_" + position, "array",
                this.getPackageName());
        String[] cities = getResources().getStringArray(resourceId);

        recyclerView.setAdapter(new CityAdapter(this, cities, region));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(cityTextView.equalsIgnoreCase(CHOICE_CITY_MESSAGE)) {
                            TextView region = (TextView)view.findViewById(R.id.region_activity_region);

                            cityTextView = region.getText().toString();
                            tvCity.setText(cityTextView);

                            fillActivityContent(region.getText().toString(), position);
                        } else {
                            TextView city = (TextView)view.findViewById(R.id.city_activity_city);
                            saveCityName(city.getText().toString());
                        }
                    }
                })
        );
    }

    private void saveCityName(String cityName) {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.city_name), cityName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
