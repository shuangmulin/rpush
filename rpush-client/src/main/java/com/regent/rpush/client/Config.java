package com.regent.rpush.client;

import cn.hutool.setting.dialect.Props;

/**
 * 配置类
 *
 * @author 钟宝林
 * @since 2021/2/27/027 0:06
 **/
public class Config {

    private static final Props props = new Props("application.properties");

    public static Long getRegistrationId() {
        return props.getLong("registrationId");
    }

    public static String getRouteHost() {
        return props.getStr("rpush.route.host");
    }

    public static Integer getRoutePort() {
        return props.getInt("rpush.route.port");
    }

}
