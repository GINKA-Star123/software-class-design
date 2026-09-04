package com.example.storyworkshop.module.play.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.example.storyworkshop.module.story.service.ConditionParser;
import org.springframework.stereotype.Component;

@Component
public class ConditionEvaluator {

    public boolean evaluate(String expr, Set<String> achCodes, Set<String> reachedEndings) {
        if (expr == null || expr.isBlank()) return true;
        for (String group : split(expr, "||")) {
            boolean ok = true;
            for (String term : split(group, "&&")) {
                ok = ok && evalTerm(term.trim(), achCodes, reachedEndings);
            }
            if (ok) return true;
        }
        return false;
    }

    private List<String> split(String expr, String op) {
        return Arrays.stream(expr.split(Pattern.quote(op))).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    private boolean evalTerm(String term, Set<String> codes, Set<String> endings) {
        String lower = term.toLowerCase();
        if (lower.equals("true")) return true;
        if (lower.equals("false")) return false;
        if (lower.startsWith(ConditionParser.ACH_PREFIX)) {
            return codes != null && codes.contains(term.substring(ConditionParser.ACH_PREFIX.length()).trim());
        }
        if (lower.startsWith(ConditionParser.END_PREFIX)) {
            return endings != null && endings.contains(term.substring(ConditionParser.END_PREFIX.length()).trim());
        }
        return false;
    }
}
