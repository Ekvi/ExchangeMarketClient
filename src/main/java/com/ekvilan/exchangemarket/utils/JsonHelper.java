package com.ekvilan.exchangemarket.utils;


import android.util.Log;

import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.models.Rates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    private String LOG_TAG = "myLog";

    private final String ID = "id";
    private final String USER_ID = "userId";
    private final String CITY = "city";
    private final String ACTION_USER_CHOICE = "action";
    private final String CURRENCY_USER_CHOICE = "currency";
    private final String SUM = "sum";
    private final String RATE = "rate";
    private final String PHONE = "phone";
    private final String AREA = "area";
    private final String COMMENT = "comment";
    private final String DATE = "date";
    private final String ACTIONS_LIST= "actions";
    private final String CURRENCIES_LIST= "currencies";
    private final String USD_BUY= "usdBuy";
    private final String USD_SALE= "usdSale";
    private final String EUR_BUY= "eurBuy";
    private final String EUR_SALE= "eurSale";
    private final String RUB_BUY= "rubBuy";
    private final String RUB_SALE= "rubSale";

    public List<Object> readJson(boolean isRates, String jsonFromServer) {
        List<Object> entities = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(jsonFromServer);

            for(int i = 0; i < array.length(); i++) {
                entities.add(createEntity(isRates, array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't parse json!");
            e.printStackTrace();
        }

        return entities;
    }

    private Object createEntity(boolean isRates, JSONObject json) throws JSONException {
        if(isRates) {
            return new Rates(json.getString(USD_BUY), json.getString(USD_SALE),
                    json.getString(EUR_BUY), json.getString(EUR_SALE),
                    json.getString(RUB_BUY), json.getString(RUB_SALE));
        } else {
            return new Advertisement(json.getLong(ID),
                    json.getString(USER_ID), json.getString(CITY),
                    json.getString(ACTION_USER_CHOICE), json.getString(CURRENCY_USER_CHOICE),
                    json.getString(SUM), json.getString(RATE), json.getString(PHONE),
                    json.getString(AREA), json.getString(COMMENT), json.getString(DATE));
        }
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
            json.put(DATE, advertisement.getDate());
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't create json file!");
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject createJson(String city, List<String> actions, List<String> currencies)  {
        JSONObject json = new JSONObject();

        try {
            json.put(CITY, city);
            json.put(ACTIONS_LIST, new JSONArray(actions));
            json.put(CURRENCIES_LIST, new JSONArray(currencies));
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't create json file!");
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject createJson(String userId)  {
        return create(USER_ID, userId);
    }

    public JSONObject createJson(long id)  {
        return create(ID, id);
    }

    private JSONObject create(String name, Object value) {
        JSONObject json = new JSONObject();

        try {
            json.put(name, value);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Can't create json file!");
            e.printStackTrace();
        }

        return json;
    }
}
