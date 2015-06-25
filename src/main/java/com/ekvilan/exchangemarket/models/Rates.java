package com.ekvilan.exchangemarket.models;


public class Rates {
    private String usdBuy;
    private String usdSale;
    private String eurBuy;
    private String eurSale;
    private String rubBuy;
    private String rubSale;

    public Rates() {}

    public Rates(String usdBuy, String usdSale, String eurBuy, String eurSale, String rubBuy, String rubSale) {
        this.usdBuy = usdBuy;
        this.usdSale = usdSale;
        this.eurBuy = eurBuy;
        this.eurSale = eurSale;
        this.rubBuy = rubBuy;
        this.rubSale = rubSale;
    }

    public String getUsdBuy() {
        return usdBuy;
    }

    public void setUsdBuy(String usdBuy) {
        this.usdBuy = usdBuy;
    }

    public String getUsdSale() {
        return usdSale;
    }

    public void setUsdSale(String usdSale) {
        this.usdSale = usdSale;
    }

    public String getEurBuy() {
        return eurBuy;
    }

    public void setEurBuy(String eurBuy) {
        this.eurBuy = eurBuy;
    }

    public String getEurSale() {
        return eurSale;
    }

    public void setEurSale(String eurSale) {
        this.eurSale = eurSale;
    }

    public String getRubBuy() {
        return rubBuy;
    }

    public void setRubBuy(String rubBuy) {
        this.rubBuy = rubBuy;
    }

    public String getRubSale() {
        return rubSale;
    }

    public void setRubSale(String rubSale) {
        this.rubSale = rubSale;
    }
}
