package com.itermit.railway.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Helper class for generating queries.
 * <p>
 * Constructs result query from four parts:
 * <p>
 * Main part, Conditions, Sorts, Offset.
 *
 * @author O.Savinov
 */
public class QueryMaker implements Serializable {

    private StringBuilder queryMain = new StringBuilder();
    private StringBuilder queryCondition = new StringBuilder();
    private StringBuilder querySort = new StringBuilder();
    private StringBuilder queryOffset = new StringBuilder();
    private int page;
    private final ArrayList<String> paramList = new ArrayList<>();

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

    /**
     * Constructs string with offset for pagination purpose.
     *
     * @param page page number
     */
    public void setQueryOffset(int page) {
        setPage(page);
        this.queryOffset = new StringBuilder(" LIMIT ")
                .append(Paginator.PAGE_SIZE)
                .append(" OFFSET ")
                .append(page * Paginator.PAGE_SIZE - Paginator.PAGE_SIZE);
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

    public void deleteQuerySort() {
        this.querySort = new StringBuilder();
    }

    /**
     * Constructs string with conditions and filters.
     *
     * @param field     String with field name
     * @param condition Condition
     * @param value     String value
     */
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

    /**
     * Constructs string with sorts.
     *
     * @param field     String with field name
     * @param condition Condition
     */
    private void updateQuerySort(String field, Condition condition) {

        if (field.equals("seats_available"))
            field = "seats_total - seats_reserved";
        if (field.equals("travel_time"))
            field = "TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival)";

        StringBuilder query = new StringBuilder(field).append(" ").append(condition.get());

        if (this.querySort.length() == 0) {
            setQuerySort(new StringBuilder(" ORDER BY ").append(query));
        } else {
            this.querySort.append(", ").append(query);
        }
    }

    /**
     * Returns PreparedStatement instance from given connection.
     * <p>
     * Dynamically fills statement parameters with values from paramList.
     *
     * @param connection Connection
     */
    public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(getFinalQuery());
        int l = 0;

        for (Object param : paramList) {
            preparedStatement.setObject(++l, param);
        }

        return preparedStatement;
    }

    /**
     * Returns final query.
     * <p>
     * Joins all parts of query together.
     * Only Main part is required, others are optional.
     *
     * @return String with final query
     */
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

