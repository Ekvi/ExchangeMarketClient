package com.ekvilan.exchangemarket.utils;


import org.junit.Test;

import static org.junit.Assert.*;


public class ValidatorTest {
    private Validator validator = new Validator();

    @Test
    public void testIsInteger() throws Exception {
        assertTrue(validator.isInteger("100"));
        assertFalse(validator.isInteger("20,5"));
        assertFalse(validator.isInteger("10.2"));
        assertFalse(validator.isInteger("test"));
    }

    @Test
    public void testIsEmptyField() throws Exception {
        assertTrue(validator.isEmptyField(""));
        assertTrue(validator.isEmptyField(null));
        assertFalse(validator.isEmptyField("test"));
    }

    @Test
    public void testIsDigit() {
        assertFalse(validator.isDigit(".89"));
        assertTrue(validator.isDigit("12.20"));
        assertTrue(validator.isDigit("100"));
        assertFalse(validator.isDigit("test"));
    }

    @Test
    public void testValidateRate() throws Exception {
        assertTrue(validator.validateRate("100"));
        assertTrue(validator.validateRate("20,5"));
        assertTrue(validator.validateRate("10.2"));
        assertFalse(validator.validateRate("test"));
        assertFalse(validator.validateRate(".90"));
    }
}
