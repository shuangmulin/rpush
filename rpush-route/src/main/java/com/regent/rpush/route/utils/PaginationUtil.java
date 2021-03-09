package com.regent.rpush.route.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.dto.table.Pagination;

/**
 * @author 钟宝林
 * @since 2021/3/9/009 20:48
 **/
public class PaginationUtil {

    public static <T> Pagination<T> convert(Page<T> page) {
        Pagination<T> pagination = new Pagination<>();
        pagination.setTotal((int) page.getTotal());
        pagination.setPageNum((int) page.getCurrent());
        pagination.setPageSize((int) page.getSize());
        pagination.setDataList(page.getRecords());
        return pagination;
    }

}
