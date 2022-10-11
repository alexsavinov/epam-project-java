package com.itermit.railway.utils;

public class Paginator {

    public static int PAGE_SIZE = 2;
    int page;
    int pages;
    Object data;

    public Paginator(int page, int pages, Object data) {
        this.page = page;
        this.pages = pages;
        this.data = data;
    }

    public int getPage() {
        return page;
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

//        if (page + 1 >= pages) {
//            return 0;
//        }
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
}