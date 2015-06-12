package com.ekvilan.exchangemarket.view.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ekvilan.exchangemarket.R;
import com.ekvilan.exchangemarket.view.adapters.CityAdapter;
import com.ekvilan.exchangemarket.view.listeners.RecyclerItemClickListener;


public class ShowCitiesActivity extends ActionBarActivity {
    private RecyclerView recyclerView;
    private TextView tvRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cities);

        initView();
        initToolBar();

        Intent intent = getIntent();
        String regionName = intent.getStringExtra(getResources().getString(R.string.region_name));
        int id = intent.getIntExtra(getResources().getString(R.string.region_id), 0);

        fillActivityContent(regionName, id);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvRegion = (TextView) findViewById(R.id.tvCity);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void fillActivityContent(String regionName, int id) {
        tvRegion.setText(regionName);
        int resourceId = getResources().getIdentifier("cities_" + id, "array",
                ShowCitiesActivity.this.getPackageName());
        String[] cities = getResources().getStringArray(resourceId);

        recyclerView.setAdapter(new CityAdapter(this, cities, regionName));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        ShowCitiesActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        TextView cityName = (TextView)view.findViewById(R.id.city_activity_city);
                        saveCityName(cityName.getText().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_cities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
