package com.itermit.railway.utils;


public enum Condition {

    GT(">"),
    EQ("="),
    LT("<"),
    LE("<="),
    GE(">="),
    ASC("ASC"),
    DESC("DESC"),
    LIKE("LIKE");
    private String action;

    private Condition(String action) {
        this.action = action;
    }

    public String get() {
        return this.action;
    }

}