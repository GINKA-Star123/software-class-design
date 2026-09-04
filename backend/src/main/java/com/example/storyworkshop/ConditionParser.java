package com.example.storyworkshop.module.story.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ConditionParser {
    public static final String ACH_PREFIX = "ach:";
    public static final String END_PREFIX = "end:";
    private static final Pattern SPLIT = Pattern.compile("&&|\\|\\|");

    private ConditionParser() {}

    public static List<String> split(String expr) {
        List<String> result = new ArrayList<>();
        if (expr == null || expr.isBlank()) return result;
        for (String t : SPLIT.split(expr)) {
            String s = t.trim();
            if (!s.isEmpty()) result.add(s);
        }
        return result;
    }

    public static boolean isValid(String expr) {
        if (expr == null || expr.isBlank()) return true;
        for (String term : split(expr)) {
            String lower = term.toLowerCase();
            if (lower.equals("true") || lower.equals("false")) continue;
            if (lower.startsWith(ACH_PREFIX) && term.length() > ACH_PREFIX.length()) continue;
            if (lower.startsWith(END_PREFIX) && term.length() > END_PREFIX.length()) continue;
            return false;
        }
        return true;
    }
}
