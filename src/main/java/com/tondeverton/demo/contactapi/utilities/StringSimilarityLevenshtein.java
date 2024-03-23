package com.tondeverton.demo.contactapi.utilities;

import java.util.StringTokenizer;

public class StringSimilarityLevenshtein implements StringSimilarityUtil {
    public double percentageBetween(String string1, String string2) {
        var tokenizer1 = new StringTokenizer(string1);
        var tokenizer2 = new StringTokenizer(string2);

        var commonTokens = 0;
        while (tokenizer1.hasMoreTokens()) {
            var token1 = tokenizer1.nextToken();
            while (tokenizer2.hasMoreTokens()) {
                var token2 = tokenizer2.nextToken();
                if (token1.equals(token2)) {
                    commonTokens++;
                    break;
                }
            }
            tokenizer2 = new StringTokenizer(string2);
        }

        var totalTokens = Math.max(tokenizer1.countTokens(), tokenizer2.countTokens());
        return ((double) commonTokens / totalTokens) * 100;
    }
}
