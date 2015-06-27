package com.ekvilan.exchangemarket.view;


import android.accounts.Account;
import android.accounts.AccountManager;

import com.ekvilan.exchangemarket.models.Advertisement;
import com.ekvilan.exchangemarket.models.Rates;

import java.util.ArrayList;
import java.util.List;


public class ActivityProvider {

    public String getUserId(AccountManager manager) {
        Account[] accounts = manager.getAccountsByType("com.google");

        String userId = "";
        if(accounts.length > 0) {
            userId = accounts[0].name;
        }

        return userId;
    }

    public List<Advertisement> transformToAdvertisements(List<Object> entities) {
        List<Advertisement> ads = new ArrayList<>();
        for(Object o : entities) {
            ads.add((Advertisement)o);
        }
        return ads;
    }

    public List<Rates> transformToRates(List<Object> entities) {
        List<Rates> rates = new ArrayList<>();
        for(Object o : entities) {
            rates.add((Rates)o);
        }
        return rates;
    }
}
