package com.regent.rpush.scheduler.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * QueryWrapper工具
 *
 * @author 钟宝林
 * @since 2021/3/9/009 15:20
 **/
public class Qw {

    public static <T> QueryWrapper<T> newInstance(Class<T> clazz) {
        return new QueryWrapper<>();
    }

}
