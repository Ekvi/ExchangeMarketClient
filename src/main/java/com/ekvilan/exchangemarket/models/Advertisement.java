package com.ekvilan.exchangemarket.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Advertisement implements Parcelable {
    private String userId;
    private String city;
    private String action;
    private String currency;
    private String sum;
    private String rate;
    private String phone;
    private String area;
    private String comment;
    private String date;

    public Advertisement() {}

    public Advertisement(Parcel parcel) {
        readFromParcel(parcel);
    }

    public Advertisement(String userId, String city, String action, String currency, String sum,
                         String rate, String phone, String area, String comment, String date) {
        this.userId = userId;
        this.city = city;
        this.action = action;
        this.currency = currency;
        this.sum = sum;
        this.rate = rate;
        this.phone = phone;
        this.area = area;
        this.comment = comment;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(city);
        dest.writeString(action);
        dest.writeString(currency);
        dest.writeString(sum);
        dest.writeString(rate);
        dest.writeString(phone);
        dest.writeString(area);
        dest.writeString(comment);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<Advertisement> CREATOR = new Parcelable.Creator<Advertisement>() {
        public Advertisement createFromParcel(Parcel parcel) {
            return new Advertisement(parcel);
        }

        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };

    private void readFromParcel(Parcel parcel) {
        userId = parcel.readString();
        city = parcel.readString();
        action = parcel.readString();
        currency = parcel.readString();
        sum = parcel.readString();
        rate = parcel.readString();
        phone = parcel.readString();
        area = parcel.readString();
        comment = parcel.readString();
        date = parcel.readString();
    }
}
