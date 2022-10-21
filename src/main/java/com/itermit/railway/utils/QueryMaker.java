package com.itermit.railway.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryMaker implements Serializable {

    private StringBuilder queryMain = new StringBuilder();
    private StringBuilder queryCondition = new StringBuilder();
    private StringBuilder querySort = new StringBuilder();
    private StringBuilder queryOffset = new StringBuilder();
    private int page;
    private ArrayList paramList = new ArrayList();

    private QueryMaker() {
    }

    public StringBuilder getQueryMain() {
        return queryMain;
    }

    public void setQueryMain(String queryMain) {
        this.queryMain = new StringBuilder(queryMain);
    }

    public StringBuilder getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(StringBuilder queryCondition) {
        this.queryCondition = new StringBuilder(queryCondition);
    }

    public StringBuilder getQuerySort() {
        return querySort;
    }

    public void setQuerySort(StringBuilder querySort) {

        this.querySort = new StringBuilder(querySort);
    }

    public StringBuilder getQueryOffset() {
        return queryOffset;
    }

    public void setQueryOffset(int page) {

        setPage(page);
        this.queryOffset = new StringBuilder(" LIMIT ")
                .append(Paginator.PAGE_SIZE)
                .append(" OFFSET ")
                .append(Integer.valueOf(page) * Paginator.PAGE_SIZE - Paginator.PAGE_SIZE);

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void deleteQueryOffset() {
        this.queryOffset = new StringBuilder();
    }

    public void updateQueryCondition(String field, Condition condition, String value) {

        if (field.equals("seats_available"))
            field = "seats_total - seats_reserved";
        if (field.equals("travel_time"))
            field = "TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival)";
        if (condition.equals(Condition.LIKE))
            value = "%" + value + "%";

        StringBuilder query = new StringBuilder(field).append(" ")
                .append(condition.get()).append(" ?");

        if (this.queryCondition.length() == 0) {
            setQueryCondition(new StringBuilder(" WHERE ").append(query));
        } else {
            this.queryCondition.append(" AND ").append(query);
        }

        paramList.add(value);
    }

    private void updateQuerySort(String field, Condition condition) {

        StringBuilder query = new StringBuilder(field).append(" ").append(condition.get());

        if (this.querySort.length() == 0) {
            setQuerySort(new StringBuilder(" ORDER BY ").append(query));
        } else {
            this.querySort.append(", ").append(query);
        }
    }

    public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(getFinalQuery());
        int l = 0;

        for (Object param : paramList) {
            preparedStatement.setObject(++l, param);
        }

        return preparedStatement;
    }

    public String getFinalQuery() {

        return queryMain
                .append(queryCondition)
                .append(querySort)
                .append(queryOffset)
                .toString();
    }

    @Override
    public String toString() {

        return "QueryMaker{" +
                "queryMain=" + queryMain +
                ", queryCondition=" + queryCondition +
                ", querySort=" + querySort +
                ", queryOffset=" + queryOffset +
                '}';
    }

    public static class Builder {

        private final QueryMaker queryMaker;

        public Builder() {
            queryMaker = new QueryMaker();
        }

        public QueryMaker.Builder withMainQuery(String query) {
            queryMaker.setQueryMain(query);
            return this;
        }

        public QueryMaker.Builder withCondition(String field, Condition condition, String value) {
            queryMaker.updateQueryCondition(field, condition, value);
            return this;
        }

        public QueryMaker.Builder withSort(String field, Condition condition) {
            queryMaker.updateQuerySort(field, condition);
            return this;
        }

        public QueryMaker.Builder withPaginator(int page) {
            queryMaker.setQueryOffset(page);
            return this;
        }

        public QueryMaker build() {
            return queryMaker;
        }
    }

}

