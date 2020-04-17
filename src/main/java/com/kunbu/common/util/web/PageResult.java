package com.kunbu.common.util.web;

import java.io.Serializable;

/**
 * @author: kunbu
 * @create: 2019-08-30 15:13
 **/
public class PageResult<T> implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Long pages;
    private T list;

    public PageResult() {
    }

    public static PageResult init(Integer pageNum, Integer pageSize) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(0L);
        pageResult.setPages(0L);
        return pageResult;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}
