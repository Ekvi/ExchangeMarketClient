package com.ekvilan.exchangemarket.utils;


import com.ekvilan.exchangemarket.models.Advertisement;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JsonHelperTest {
    private JsonHelper jsonHelper = new JsonHelper();


    @Test
    public void testReadJson() {
        String json = "[{\"userId\":\"test@gmail.com\",\"city\":\"Odessa\", \"actionUserChoice\":\"покупка\", \"currencyUserChoice\":\"usd\", \"sum\":\"200\", \"rate\":\"20.50\", \"phone\":\"80976571223\", \"area\":\"Таирова\", \"comment\":\"можно частями\"}, {\"userId\":\"secondUser@gmail.com\",\"city\":\"Odessa\", \"actionUserChoice\":\"покупка\", \"currencyUserChoice\":\"eur\", \"sum\":\"500\", \"rate\":\"25.50\", \"phone\":\"80976571223\", \"area\":\"Центр\", \"comment\":\"могу подьехать\"}]";

        List<Advertisement> advertisements = jsonHelper.readJson(json);

        assertTrue(!advertisements.isEmpty());
        assertEquals(2, advertisements.size());

        assertEquals("Odessa", advertisements.get(0).getCity());
        assertEquals("покупка", advertisements.get(0).getAction());
        assertEquals("200", advertisements.get(0).getSum());
        assertEquals("80976571223", advertisements.get(1).getPhone());
        assertEquals("могу подьехать", advertisements.get(1).getComment());
    }

    @Test
    public void testCreateJson() throws JSONException {
        Advertisement advertisement = new Advertisement("userId", "Odessa", "sale", "usd",
                "sum", "rate", "phone", "area", "comment");

        JSONObject json = jsonHelper.createJson(advertisement);

        assertEquals(9, json.length());
        assertEquals("Odessa", json.getString("city"));
        assertEquals("usd", json.getString("currencyUserChoice"));
    }
}
