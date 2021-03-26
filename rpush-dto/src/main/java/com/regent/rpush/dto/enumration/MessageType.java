package com.regent.rpush.dto.enumration;

/**
 * 消息类型枚举
 *
 * @author 钟宝林
 * @since 2021/3/25/025 21:37
 **/
public enum MessageType {

    EMAIL(MessagePlatformEnum.EMAIL),
    RPUSH_SERVER(MessagePlatformEnum.RPUSH_SERVER),
    WECHAT_WORK_TEXT(MessagePlatformEnum.WECHAT_WORK);

    private final MessagePlatformEnum platform;

    MessageType(MessagePlatformEnum platform) {
        this.platform = platform;
    }

    public MessagePlatformEnum getPlatform() {
        return platform;
    }
}
