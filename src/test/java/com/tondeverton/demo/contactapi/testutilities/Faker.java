package com.tondeverton.demo.contactapi.testutilities;

import org.fluttercode.datafactory.impl.DataFactory;

public class Faker {

    private static final DataFactory dataFactory = new DataFactory();

    public static String firstName() {
        return dataFactory.getFirstName();
    }

    public static String lastName() {
        return dataFactory.getLastName();
    }

    public static String nickname() {
        return dataFactory.getRandomWord();
    }

    public static String phoneNumber() {
        return dataFactory.getNumberText(12);
    }

    public static String email() {
        return dataFactory.getEmailAddress();
    }

    public static String word() {
        return dataFactory.getRandomWord();
    }

    public static String word(int length) {
        return dataFactory.getRandomWord(length);
    }

    public static String number(int digits) {
        return dataFactory.getNumberText(digits);
    }

    public static int intBetween(int min, int max) {
        return dataFactory.getNumberBetween(min, max);
    }
}
