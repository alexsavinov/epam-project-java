package com.itermit.railway.utils;

import java.io.IOException;
import java.util.Properties;

public class Paginator {

    private static Properties conf;

    static {
        try {
            conf = PropertiesLoader.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int PAGE_SIZE = Integer.parseInt(conf.getProperty("pagination.page.size"));
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

    public int getNext_page() {
        return Math.min(page + 1, pages);
    }

    public int getPrev_page() {
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
                "page=" + page +
                ", next_page=" + getPage() +
                ", prev_page=" + getNext_page() +
                ", pages=" + pages +
                ", data=" + data +
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