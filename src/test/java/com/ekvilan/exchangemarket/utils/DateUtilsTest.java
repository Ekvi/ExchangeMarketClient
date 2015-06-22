package com.ekvilan.exchangemarket.utils;


import org.junit.Test;


import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void testFormatDate() {
        DateUtils dateUtils = new DateUtils();

        assertEquals("2015-06-22", dateUtils.formatDate("2015-06-22 09:59", "2015-04-12 14:34"));
        assertEquals("09:59", dateUtils.formatDate("2015-06-22 09:59", "2015-06-22 14:34"));
    }
}
