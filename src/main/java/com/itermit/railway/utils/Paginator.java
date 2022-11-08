package com.itermit.railway.utils;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Pagination for handy display large amount of data.
 *
 * @author O.Savinov
 */
public class Paginator {

    private static Properties conf;
    public static int PAGE_SIZE = 5;

    static {
        try {
            conf = PropertiesLoader.loadProperties();
            if (!conf.isEmpty()) {
                PAGE_SIZE = Integer.parseInt(conf.getProperty("pagination.page.size"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paginator)) return false;
        Paginator paginator = (Paginator) o;
        return getPage() == paginator.getPage() && getPages() == paginator.getPages() && getResults() == paginator.getResults() && Objects.equals(getData(), paginator.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPage(), getPages(), getResults(), getData());
    }

    int page;
    int pages;
    int results;
    Object data;

    private Paginator() {
    }

    public int getPage() {
        return page;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public boolean pageIsFirst() {
        return page == 1;
    }

    public boolean pageIsLast() {
        return page == pages;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNextPage() {
        return Math.min(page + 1, pages);
    }

    public int getPrevPage() {
        return Math.max(page - 1, 0);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Paginator{" +
                "page=" + getPage() +
                ", pages=" + getPages() +
                ", prevPage=" + getPrevPage() +
                ", nextPage=" + getNextPage() +
                ", data=" + getData() +
                ", results=" + getResults() +
                '}';
    }

    public static class Builder {
        private final Paginator paginator;

        public Builder() {
            paginator = new Paginator();
        }

        public Paginator.Builder withPage(int page) {
            paginator.setPage(page);
            return this;
        }

        public Paginator.Builder withPages(int pages) {
            paginator.setPages(pages);
            return this;
        }

        public Paginator.Builder withResults(int results) {
            paginator.setResults(results);
            return this;
        }

        public Paginator.Builder withData(Object data) {
            paginator.setData(data);
            return this;
        }

        public Paginator build() {
            return paginator;
        }
    }

}