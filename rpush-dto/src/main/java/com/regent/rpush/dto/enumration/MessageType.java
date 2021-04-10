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

    // ================================企业微信====================================
    WECHAT_WORK_TEXT("文本", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_IMAGE("图片", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_VIDEO("视频", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_FILE("文件", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_TEXTCARD("文本卡片", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_NEWS("图文消息", MessagePlatformEnum.WECHAT_WORK),
    WECHAT_WORK_MARKDOWN("Markdown", MessagePlatformEnum.WECHAT_WORK),

    // ================================微信公众号====================================
    WECHAT_OFFICIAL_ACCOUNT_TEXT("文本", MessagePlatformEnum.WECHAT_OFFICIAL_ACCOUNT),
    WECHAT_OFFICIAL_ACCOUNT_NEWS("图文消息", MessagePlatformEnum.WECHAT_OFFICIAL_ACCOUNT),
    WECHAT_OFFICIAL_ACCOUNT_TEMPLATE("模板消息", MessagePlatformEnum.WECHAT_OFFICIAL_ACCOUNT),

    ;



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
