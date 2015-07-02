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
        String json = "[{\"id\":\"24\",\"userId\":\"test@gmail.com\",\"city\":\"Odessa\", \"action\":\"покупка\", \"currency\":\"usd\", \"sum\":\"200\", \"rate\":\"20.50\", \"phone\":\"80976571223\", \"area\":\"Таирова\", \"comment\":\"можно частями\", \"date\":\"7 июня 2015\"}, {\"id\":\"25\",\"userId\":\"secondUser@gmail.com\",\"city\":\"Odessa\", \"action\":\"покупка\", \"currency\":\"eur\", \"sum\":\"500\", \"rate\":\"25.50\", \"phone\":\"80976571223\", \"area\":\"Центр\", \"comment\":\"могу подьехать\", \"date\":\"7 июня 2015\"}]";

        List<Object> advertisements = jsonHelper.readJson(false, json);
        Advertisement ad1 = (Advertisement)advertisements.get(0);
        Advertisement ad2 = (Advertisement)advertisements.get(1);

        assertTrue(!advertisements.isEmpty());
        assertEquals(2, advertisements.size());

        assertEquals("Odessa", ad1.getCity());
        assertEquals("покупка", ad1.getAction());
        assertEquals("200", ad1.getSum());
        assertEquals("80976571223", ad2.getPhone());
        assertEquals("могу подьехать", ad2.getComment());
    }

    @Test
    public void testCreateJson() throws JSONException {
        Advertisement advertisement = new Advertisement("userId", "Odessa", "sale", "usd",
                "sum", "rate", "phone", "area", "comment", "date");

        JSONObject json = jsonHelper.createJson(advertisement);

        assertEquals(10, json.length());
        assertEquals("Odessa", json.getString("city"));
        assertEquals("usd", json.getString("currency"));
    }
}
