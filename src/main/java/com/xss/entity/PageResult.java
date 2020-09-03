package com.xss.entity;

import java.util.List;

/**
 * 前后端分页结果集
 * @param <T>
 */
public class PageResult<T> {

    // 页大小
    public static final Integer PAGE_SIZE=5;

    // 总页数
    private Integer totalPage;

    // 当前页数据
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Integer totalPage, List<T> rows) {
        this.totalPage = totalPage;
        this.rows = rows;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
