package com.regent.rpush.common;

/**
 * @author 钟宝林
 * @since 2021/3/6/006 17:36
 **/
public class PageUtil {

    /**
     * 规范系统默认分页当前页
     */
    public static int getDefaultPageNum(Integer pageNum) {
        return pageNum == null || pageNum <= 0 ? 1 : pageNum;
    }

    /**
     * 规范系统默认分页
     */
    public static int getDefaultPageSize(Integer pageSize) {
        return pageSize == null || pageSize <= 0 ? 20 :pageSize;
    }

}
