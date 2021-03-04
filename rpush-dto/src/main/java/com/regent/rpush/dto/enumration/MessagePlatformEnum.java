package com.regent.rpush.dto.enumration;

/**
 * 消息平台枚举
 *
 * @author 钟宝林
 * @date 2021/2/8 9:36
 **/
public enum MessagePlatformEnum {

    EMAIL("邮箱", ""),
    //    WECHAT_WORK("企业微信", "),
    //    DING_TALK("钉钉", "),
    RPUSH_SERVER("rpush服务", "");

    private final String alias;
    private final String description;

    MessagePlatformEnum(String alias, String description) {
        this.alias = alias;
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }
}
