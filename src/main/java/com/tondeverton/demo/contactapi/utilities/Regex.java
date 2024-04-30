package com.tondeverton.demo.contactapi.utilities;

public class Regex implements RegexUtil {
    @Override
    public String removeAlphaCharacter(String target) {
        return target.replaceAll("[^0-9]", "");
    }

    public static RegexUtil getRegexUtil() {
        return new Regex();
    }
}
