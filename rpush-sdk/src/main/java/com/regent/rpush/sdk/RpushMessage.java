package com.regent.rpush.sdk;

import com.regent.rpush.dto.enumration.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类
 *
 * @author 钟宝林
 * @since 2021/6/14/014 10:49
 **/
public class RpushMessage {

    /**
    * 消息参数和消息类型的映射关系，键为消息参数类型，值为对应的消息类型
    */
    static final Map<Class<?>, MessageType> MESSAGE_TYPE_PARAM_MAP = new HashMap<>();
    
    /**
     * 邮箱普通邮件 
     */
    public static com.regent.rpush.dto.message.EmailMessageDTO.EmailMessageDTOBuilder<?, ?> EMAIL() {
        return com.regent.rpush.dto.message.EmailMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.EmailMessageDTO.class, MessageType.EMAIL);
    }

    /**
     * rpush服务文本
     */
    public static com.regent.rpush.dto.message.RpushMessageDTO.RpushMessageDTOBuilder<?, ?> RPUSH_SERVER() {
        return com.regent.rpush.dto.message.RpushMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.RpushMessageDTO.class, MessageType.RPUSH_SERVER);
    }

    /**
     * 钉钉-工作通知卡片-多按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.ActionCardMultiMessageDTOBuilder<?, ?> DING_TALK_COPR_ACTION_CARD_MULTI() {
        return com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.class, MessageType.DING_TALK_COPR_ACTION_CARD_MULTI);
    }

    /**
     * 钉钉-工作通知卡片-单按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.ActionCardSingleMessageDTOBuilder<?, ?> DING_TALK_COPR_ACTION_CARD_SINGLE() {
        return com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.class, MessageType.DING_TALK_COPR_ACTION_CARD_SINGLE);
    }

    /**
     * 钉钉-工作通知链接消息
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.LinkMessageDTOBuilder<?, ?> DING_TALK_COPR_LINK() {
        return com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.class, MessageType.DING_TALK_COPR_LINK);
    }

    /**
     * 钉钉-工作通知Markdown
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> DING_TALK_COPR_MARKDOWN() {
        return com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.class, MessageType.DING_TALK_COPR_MARKDOWN);
    }

    /**
     * 钉钉-工作通知OA消息
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.OaMessageDTOBuilder<?, ?> DING_TALK_COPR_OA() {
        return com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.class, MessageType.DING_TALK_COPR_OA);
    }

    /**
     * 钉钉-工作通知文本
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.TextMessageDTOBuilder<?, ?> DING_TALK_COPR_TEXT() {
        return com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.class, MessageType.DING_TALK_COPR_TEXT);
    }

    /**
     * 钉钉-群机器人卡片-多按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.ActionCardMultiMessageDTO.ActionCardMultiMessageDTOBuilder<?, ?> DING_TALK_ROBOT_ACTION_CARD_MULTI() {
        return com.regent.rpush.dto.message.dingtalk.robot.ActionCardMultiMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.ActionCardMultiMessageDTO.class, MessageType.DING_TALK_ROBOT_ACTION_CARD_MULTI);
    }

    /**
     * 钉钉-群机器人卡片-单按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.ActionCardSingleMessageDTO.ActionCardSingleMessageDTOBuilder<?, ?> DING_TALK_ROBOT_ACTION_CARD_SINGLE() {
        return com.regent.rpush.dto.message.dingtalk.robot.ActionCardSingleMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.ActionCardSingleMessageDTO.class, MessageType.DING_TALK_ROBOT_ACTION_CARD_SINGLE);
    }

    /**
     * 钉钉-群机器人FeedCard
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.FeedCardMessageDTO.FeedCardMessageDTOBuilder<?, ?> DING_TALK_ROBOT_FEED_CARD() {
        return com.regent.rpush.dto.message.dingtalk.robot.FeedCardMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.FeedCardMessageDTO.class, MessageType.DING_TALK_ROBOT_FEED_CARD);
    }

    /**
     * 钉钉-群机器人链接消息
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.LinkMessageDTO.LinkMessageDTOBuilder<?, ?> DING_TALK_ROBOT_LINK() {
        return com.regent.rpush.dto.message.dingtalk.robot.LinkMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.LinkMessageDTO.class, MessageType.DING_TALK_ROBOT_LINK);
    }

    /**
     * 钉钉-群机器人Markdown
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> DING_TALK_ROBOT_MARKDOWN() {
        return com.regent.rpush.dto.message.dingtalk.robot.MarkdownMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.MarkdownMessageDTO.class, MessageType.DING_TALK_ROBOT_MARKDOWN);
    }

    /**
     * 钉钉-群机器人文本
     */
    public static com.regent.rpush.dto.message.dingtalk.robot.TextMessageDTO.TextMessageDTOBuilder<?, ?> DING_TALK_ROBOT_TEXT() {
        return com.regent.rpush.dto.message.dingtalk.robot.TextMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.robot.TextMessageDTO.class, MessageType.DING_TALK_ROBOT_TEXT);
    }

    /**
     * 微信公众号图文消息
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_NEWS() {
        return com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_NEWS);
    }

    /**
     * 微信公众号文本
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_TEXT() {
        return com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_TEXT);
    }

    /**
     * 微信公众号模板消息
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.TemplateMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_TEMPLATE() {
        return com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_TEMPLATE);
    }

    /**
     * 企业微信-应用消息文件
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.MediaMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_FILE() {
        return com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.class, MessageType.WECHAT_WORK_AGENT_FILE);
    }

    /**
     * 企业微信-应用消息图片
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.MediaMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_IMAGE() {
        return com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.class, MessageType.WECHAT_WORK_AGENT_IMAGE);
    }

    /**
     * 企业微信-应用消息Markdown
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_MARKDOWN() {
        return com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.class, MessageType.WECHAT_WORK_AGENT_MARKDOWN);
    }

    /**
     * 企业微信-应用消息图文消息
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_NEWS() {
        return com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.class, MessageType.WECHAT_WORK_AGENT_NEWS);
    }

    /**
     * 企业微信-应用消息文本卡片
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.TextCardMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_TEXTCARD() {
        return com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.class, MessageType.WECHAT_WORK_AGENT_TEXTCARD);
    }

    /**
     * 企业微信-应用消息文本
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_TEXT() {
        return com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.class, MessageType.WECHAT_WORK_AGENT_TEXT);
    }

    /**
     * 企业微信-应用消息视频
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.VideoMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_VIDEO() {
        return com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.class, MessageType.WECHAT_WORK_AGENT_VIDEO);
    }

    /**
     * 企业微信-群机器人图片
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.ImageMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_IMAGE() {
        return com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_IMAGE);
    }

    /**
     * 企业微信-群机器人Markdown
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_MARKDOWN() {
        return com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_MARKDOWN);
    }

    /**
     * 企业微信-群机器人图文消息
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_NEWS() {
        return com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_NEWS);
    }

    /**
     * 企业微信-群机器人文本
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_TEXT() {
        return com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_TEXT);
    }

}
