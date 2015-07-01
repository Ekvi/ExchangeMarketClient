package com.ekvilan.exchangemarket.view;


public enum Urls {
    ADVERTISEMENT_ADD("http://exchangemarket-ekvi.rhcloud.com/advertisement/add"),
    ADVERTISEMENT_REMOVE("http://exchangemarket-ekvi.rhcloud.com/advertisement/remove"),
    ADVERTISEMENT_GET("http://exchangemarket-ekvi.rhcloud.com/advertisement/get"),
    ADVERTISEMENT_GET_OWN("http://exchangemarket-ekvi.rhcloud.com/advertisement/getOwn"),
    RATES_GET("http://exchangemarket-ekvi.rhcloud.com/rates/get");

    private String value;

    private Urls(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
