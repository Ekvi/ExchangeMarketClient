package com.ekvilan.exchangemarket.utils;



import com.ekvilan.exchangemarket.view.activities.ShowEntitiesActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    boolean isInteger(String sum) {
        try{
            Integer.parseInt(sum);
            return true;
        } catch(NumberFormatException exc) {
            return false;
        }
    }

    boolean isDouble(String rate) {
        rate = rate.replaceAll(",", ".");

        try{
            Double.parseDouble(rate);
            return true;
        } catch(NumberFormatException exc) {
            return false;
        }
    }

    boolean isDigit(String rate) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(rate.substring(0, 1));

        return matcher.find();
    }

    public boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public boolean validateSum(String content) {
        return isInteger(content);
    }

    public boolean validateRate(String content) {
        return !(!isDouble(content) && !isInteger(content)) && isDigit(content);
    }

    public boolean isEmptyField(String content) {
        return isEmpty(content);
    }

    public boolean isEmptyCity(String cityName) {
        return cityName.equalsIgnoreCase(ShowEntitiesActivity.CHOICE_CITY_MESSAGE);
    }

    public boolean isCorrectPhoneNumber(String number) {
        Pattern pattern = Pattern.compile("^\\+380[0-9]{2}[0-9]{7}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }
}
