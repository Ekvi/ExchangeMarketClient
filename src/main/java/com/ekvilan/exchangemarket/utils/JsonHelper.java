package com.ekvilan.exchangemarket.utils;


import android.util.Log;

import com.ekvilan.exchangemarket.models.Advertisement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    private String LOG_TAG = "market";

    private final String USER_ID = "userId";
    private final String CITY = "city";
    private final String ACTION_USER_CHOICE = "actionUserChoice";
    private final String CURRENCY_USER_CHOICE = "currencyUserChoice";
    private final String SUM = "sum";
    private final String RATE = "rate";
    private final String PHONE = "phone";
    private final String AREA = "area";
    private final String COMMENT = "comment";

    public List<Advertisement> readJson(String jsonFromServer) {
        List<Advertisement> ads = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(jsonFromServer);

            for(int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                Advertisement advertisement = new Advertisement(json.getString(USER_ID),
                        json.getString(CITY), json.getString(ACTION_USER_CHOICE),
                        json.getString(CURRENCY_USER_CHOICE), json.getString(SUM),
                        json.getString(RATE), json.getString(PHONE), json.getString(AREA),
                        json.getString(COMMENT));

                ads.add(advertisement);
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't parse json!");
            e.printStackTrace();
        }
        return ads;
    }

    public JSONObject createJson(Advertisement advertisement)  {
        JSONObject json = new JSONObject();

        try {
            json.put(USER_ID, advertisement.getUserId());
            json.put(CITY, advertisement.getCity());
            json.put(ACTION_USER_CHOICE, advertisement.getAction());
            json.put(CURRENCY_USER_CHOICE, advertisement.getCurrency());
            json.put(SUM, advertisement.getSum());
            json.put(RATE, advertisement.getRate());
            json.put(PHONE, advertisement.getPhone());
            json.put(AREA, advertisement.getArea());
            json.put(COMMENT, advertisement.getComment());
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't create json file!");
            e.printStackTrace();
        }

        return json;
    }
}