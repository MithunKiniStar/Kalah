package com.org.kalah.config;


public class KalahGameConfiguration {

    public static final int INITIAL_STONES_QUANTITY = 6;

    public static final int FIRST_PIT_INDEX = 1;

    public static final int LAST_PIT_INDEX = 14;

    private KalahGameConfiguration() {
        throw new AssertionError("Current class can not be instantiated");
    }
}
