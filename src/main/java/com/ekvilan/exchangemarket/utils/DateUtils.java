package com.ekvilan.exchangemarket.utils;



import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private final String FORMAT_WITH_TIME = "yyyy-MM-dd HH:mm";

    public String createDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_WITH_TIME);

        return sdf.format(new Date());
    }

    public String formatDate(String dateFromDb, String todayDate) {
        String day = extractDay(dateFromDb);
        String todayDay = extractDay(todayDate);

        if(day.equals(todayDay)) {
            return getTime(dateFromDb);
        } else {
            return day;
        }
    }

    private String extractDay(String date) {
        String[] str = split(date, " ");
        return str[0];
    }

    private String getTime(String date) {
        String[] str = split(date, " ");
        return str[1];
    }

    private String[] split(String date, String regexp) {
        return date.split(regexp);
    }
}
