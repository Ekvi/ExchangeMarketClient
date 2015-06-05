package com.ekvilan.exchangemarket.utils;



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
}
