package com.ekvilan.exchangemarket.view;


import android.accounts.Account;
import android.accounts.AccountManager;


public class ActivityProvider {

    public String getUserId(AccountManager manager) {
        Account[] accounts = manager.getAccountsByType("com.google");

        String userId = "";
        if(accounts.length > 0) {
            userId = accounts[0].name;
        }

        return userId;
    }
}
