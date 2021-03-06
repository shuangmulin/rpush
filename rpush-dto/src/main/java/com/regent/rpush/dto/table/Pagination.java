package com.regent.rpush.dto.table;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据
 *
 * @author 钟宝林
 * @since 2021/3/6/006 15:31
 **/
public class Pagination<E> implements Serializable {
    private static final long serialVersionUID = 6038596538766928799L;

    private static final int PAGE_ITEM_COUNT = 10; // 显示几个页码

    private List<E> dataList;
    private int total;
    private int pageSize = 20;
    private int pageNum = 1; // 当前页码
    private Integer totalPage;

    public Pagination() {
    }

    public Pagination(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Pagination(int pageNum, int pageSize, int total, List<E> dataList) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.dataList = dataList;
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        if (totalPage != null) {
            return totalPage;
        }
        if (total % pageSize == 0) {
            totalPage = total / pageSize;
        } else {
            totalPage = total / pageSize + 1;
        }
        return totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize == 0 ? 20 : pageSize;
    }

    public int getPageNum() {
        return pageNum == 0 ? 1 : pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum == 0 ? 1 : pageNum;
    }

    public int getPrevious() {
        int previous;
        if (pageNum <= 1) {
            previous = 1;
        } else {
            previous = pageNum - 1;
        }
        return previous;
    }

    public int getNext() {
        int next;
        if (pageNum + 1 >= getTotalPage()) {
            next = getTotalPage();
        } else {
            next = pageNum + 1;
        }
        return next;
    }

    public int getStartIndex() {
        return (pageNum - 1) * pageSize;
    }

    public int getEndIndex() {
        int endIndex = getStartIndex() + pageSize;
        if (endIndex > total) {
            endIndex = getStartIndex() + (total % pageSize);
        }
        return endIndex;
    }

    public int[] getPageBar() {
        int startPage;
        int endPage;
        int[] pageBar;
        if (this.getTotalPage() <= PAGE_ITEM_COUNT) { //当总页码不足或等于既定页面大小时
            pageBar = new int[this.totalPage];
            startPage = 1;
            endPage = this.totalPage;
        } else { //当总页码大于既定页面大小时
            pageBar = new int[PAGE_ITEM_COUNT];
            startPage = this.pageNum - (PAGE_ITEM_COUNT / 2 - 1);    //为了保证当前页在中间
            endPage = this.pageNum + PAGE_ITEM_COUNT / 2;

            if (startPage < 1) {
                startPage = 1;
                endPage = PAGE_ITEM_COUNT;
            }

            if (endPage > this.totalPage) {
                endPage = this.totalPage;
                startPage = this.totalPage - (PAGE_ITEM_COUNT - 1);
            }
        }

        int index = 0;
        for (int i = startPage; i <= endPage; i++) {
            pageBar[index++] = i;
        }

        return pageBar;
    }

}
