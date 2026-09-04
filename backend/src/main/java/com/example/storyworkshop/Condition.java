package com.example.storyworkshop.module.story.entity;

import java.util.ArrayList;
import java.util.List;

public class Condition {
    private String rawExpr;
    private java.util.List<String> terms;
    private String kind;
    private String value;

    public String getRawExpr() {
        return rawExpr;
    }

    public void setRawExpr(String rawExpr) {
        this.rawExpr = rawExpr;
    }

    public java.util.List<String> getTerms() {
        return terms;
    }

    public void setTerms(java.util.List<String> terms) {
        this.terms = terms;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Condition() {
        this.terms = new ArrayList<>();
    }
}
