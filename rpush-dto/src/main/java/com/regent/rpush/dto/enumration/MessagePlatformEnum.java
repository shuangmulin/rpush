package com.regent.rpush.dto.enumration;

/**
 * 消息平台枚举
 *
 * @author 钟宝林
 * @date 2021/2/8 9:36
 **/
public enum MessagePlatformEnum {

    EMAIL("邮箱", "", true),
    //    WECHAT_WORK("企业微信", ", true),
    //    DING_TALK("钉钉", "", true),
    RPUSH_SERVER("rpush服务", "", true);

    private final String name;
    private final String description;
    private final boolean enable;

    MessagePlatformEnum(String name, String description, boolean enable) {
        this.name = name;
        this.description = description;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnable() {
        return enable;
    }
}
