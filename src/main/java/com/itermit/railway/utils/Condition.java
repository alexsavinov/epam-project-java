package com.itermit.railway.utils;

/**
 * Names and values of query conditions and orders.
 *
 * @author O.Savinov
 */
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