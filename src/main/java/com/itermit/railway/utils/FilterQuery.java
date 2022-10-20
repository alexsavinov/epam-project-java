package com.itermit.railway.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterQuery implements Serializable {
    String field;
    String condition;
    ArrayList<Object> values;

    public FilterQuery(String field) {
        this.field = field;
        this.values = new ArrayList<>();
        this.condition = "IN";
    }

    public FilterQuery(String field, String condition) {
        this.field = field;
//        this.value = new ArrayList<>();
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public static void addFilter(ArrayList<FilterQuery> filters, String field, Object value) {

        FilterQuery filter = null;

        for (FilterQuery f : filters) {
            if (f.getField().equals(field)) {
                filter = f;
                break;
            }
        }

        if (filter == null) filter = new FilterQuery(field);
        filter.addValue(value);

        filters.add(filter);
    }

    public static void addFilter(ArrayList<FilterQuery> filters, String field, String condition, Object value) {

        FilterQuery filter = null;

        for (FilterQuery f : filters) {
            if (f.getField().equals(field) && f.getCondition().equals(condition)) {
                filter = f;
                break;
            }
        }

        if (filter == null) filter = new FilterQuery(field, condition);
        filter.addValue(value);

        filters.add(filter);
    }

    public static ArrayList<FilterQuery> getList() {
        return new ArrayList<>();
    }

    public void addValue(Object value) {
        values.add(value);
    }


    public FilterQuery(String field, ArrayList<Object> values) {
        this.field = field;
        this.values = values;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public void setValues(ArrayList<Object> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "field='" + field + '\'' +
                ", condition='" + condition + '\'' +
                ", values=" + values +
                '}';
    }

}

