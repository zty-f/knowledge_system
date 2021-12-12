package com.zty.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

public class PageReq {
    @NotEmpty(message = "[页码]不能为空")
    private int page;

    @NotEmpty(message = "[每页条数]不能为空")
    @Max(value = 1000, message = "[每页条数]不能超过1000")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}