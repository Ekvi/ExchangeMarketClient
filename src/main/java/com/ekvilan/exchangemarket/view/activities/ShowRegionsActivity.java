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

import com.ekvilan.exchangemarket.view.adapters.RegionAdapter;
import com.ekvilan.exchangemarket.view.listeners.RecyclerItemClickListener;



public class ShowRegionsActivity extends ActionBarActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_regions);

        initView();
        initToolBar();
        fillActivityContent();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void fillActivityContent() {
        String[] regions = getResources().getStringArray(R.array.ukrainian_regions);

        recyclerView.setAdapter(new RegionAdapter(this, regions));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        TextView region = (TextView)view.findViewById(R.id.region_activity_region);
                        startShowCitiesActivity(region.getText().toString(), position);
                    }
                })
        );
    }

    private void startShowCitiesActivity(String regionName, int position) {
        Intent intent = new Intent(this, ShowCitiesActivity.class);
        intent.putExtra(getResources().getString(R.string.region_name), regionName);
        intent.putExtra(getResources().getString(R.string.region_id), position);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {return;}
        saveCityName(intent.getStringExtra(getResources().getString(R.string.city_name)));
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
