package com.regent.rpush.dto.enumration;

/**
 * 消息类型枚举
 *
 * @author 钟宝林
 * @since 2021/3/25/025 21:37
 **/
public enum MessageType {

    EMAIL("普通邮件 ", MessagePlatformEnum.EMAIL),
    RPUSH_SERVER("文本", MessagePlatformEnum.RPUSH_SERVER),
    WECHAT_WORK_TEXT("文本", MessagePlatformEnum.WECHAT_WORK);

    /**
     * 所属平台
     */
    private final MessagePlatformEnum platform;
    private final String name;

    MessageType(String name, MessagePlatformEnum platform) {
        this.name = name;
        this.platform = platform;
    }

    public MessagePlatformEnum getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }
}
