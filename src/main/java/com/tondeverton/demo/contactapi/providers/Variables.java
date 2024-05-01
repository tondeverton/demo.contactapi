package com.tondeverton.demo.contactapi.providers;

public enum Variables {
    CONTACTS_SEARCH_MAX_PAGE_SIZE("CONTACTS_SEARCH_MAX_PAGE_SIZE", 30),
    CONTACTS_SEARCH_MIN_PERCENT_SIMILARITY("CONTACTS_SEARCH_MIN_PERCENT_SIMILARITY", 15);

    private final String name;
    private final int fallbackValue;

    Variables(String name, int fallbackValue) {
        this.name = name;
        this.fallbackValue = fallbackValue;
    }

    public String getName() {
        return name;
    }

    public int getFallbackValue() {
        return fallbackValue;
    }
}
