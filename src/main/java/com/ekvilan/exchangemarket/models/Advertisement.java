package com.ekvilan.exchangemarket.models;


public class Advertisement {
    private String city;
    private String action;
    private String currency;
    private String sum;
    private String rate;
    private String phone;
    private String area;
    private String comment;

    public Advertisement(String city, String action, String currency, String sum,
                         String rate, String phone, String area, String comment) {
        this.city = city;
        this.action = action;
        this.currency = currency;
        this.sum = sum;
        this.rate = rate;
        this.phone = phone;
        this.area = area;
        this.comment = comment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
