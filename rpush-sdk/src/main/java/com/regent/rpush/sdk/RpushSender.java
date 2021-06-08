package com.regent.rpush.sdk;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Singleton;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.base.BaseMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Rpush消息投递器
 *
 * @author 钟宝林
 * @since 2021/5/18/018 18:51
 **/
public class RpushSender {

    private static final String DEFAULT_BASE_URL = "http://159.75.121.163";

    private final String baseUrl;
    private final String clientId;
    private final String clientSecret;

    public static TimedCache<String, String> accessTokenTimedCache;

    public static RpushSender instance(String clientId, String clientSecret) {
        return Singleton.get(RpushSender.class, clientId, clientSecret);
    }

    public static RpushSender instance(String baseUrl, String clientId, String clientSecret) {
        return Singleton.get(RpushSender.class, baseUrl, clientId, clientSecret);
    }

    private RpushSender(String clientId, String clientSecret) {
        this(DEFAULT_BASE_URL, clientId, clientSecret);
    }

    private RpushSender(String baseUrl, String clientId, String clientSecret) {
        this.baseUrl = baseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private TimedCache<String, String> getAccessTokenTimedCache() {
        if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
            synchronized (RpushSender.class) {
                if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
                    String result = HttpRequest.post(baseUrl + "/rpush-route/oauth/token?grant_type=client_credentials&scope=all&client_id=" + clientId + "&client_secret=" + clientSecret)
                            .header("Accept", "application/json")
                            .timeout(30000)//超时，毫秒
                            .execute().body();
                    JSONObject jsonObject = new JSONObject(result);
                    String accessToken = jsonObject.getStr("access_token");
                    if (StringUtils.isBlank(accessToken)) {
                        throw new IllegalStateException("获取token失败，" + jsonObject);
                    }
                    Long expiresIn = jsonObject.getLong("expires_in");
                    accessTokenTimedCache = CacheUtil.newTimedCache(TimeUnit.SECONDS.toMillis(expiresIn - 5));
                    accessTokenTimedCache.put("accessToken", accessToken);
                }
            }
        }
        return accessTokenTimedCache;
    }

    public String getAccessToken() {
        return getAccessTokenTimedCache().get("accessToken");
    }

    public String sendMessage(BaseMessage... messages) {
        Map<String, Object> params = new HashMap<>();
        for (BaseMessage message : messages) {
            MessageType messageType = MESSAGE_TYPE_PARAM_MAP.get(message.getClass());
            List<Long> configIds = message.getConfigIds();

            Map<String, Object> param = new HashMap<>();
            param.put("configIds", configIds);
            param.put("param", message);
            params.put(messageType.name(), param);
        }

        JSONObject body = new JSONObject();
        body.put("messageParam", params);

        return HttpRequest.post(baseUrl + "/rpush-route/message/push")
                .header("Authorization", "Bearer " + getAccessToken())
                .body(body)
                .timeout(30000)//超时，毫秒
                .execute().body();
    }

    /**
     * 消息参数和消息类型的映射关系，键为消息参数类型，值为对应的消息类型
     */
    private static final Map<Class<?>, MessageType> MESSAGE_TYPE_PARAM_MAP = new HashMap<>();
    
    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.EmailMessageDTO.class, MessageType.EMAIL);
    }
    /**
     * 邮箱普通邮件 
     */
    public static com.regent.rpush.dto.message.EmailMessageDTO.EmailMessageDTOBuilder<?, ?> EMAIL() {
        return com.regent.rpush.dto.message.EmailMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.SocketMessageDTO.class, MessageType.RPUSH_SERVER);
    }
    /**
     * rpush服务文本
     */
    public static com.regent.rpush.dto.message.SocketMessageDTO.SocketMessageDTOBuilder<?, ?> RPUSH_SERVER() {
        return com.regent.rpush.dto.message.SocketMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.class, MessageType.DING_TALK_COPR_ACTION_CARD_MULTI);
    }
    /**
     * 钉钉-工作通知卡片-多按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.ActionCardMultiMessageDTOBuilder<?, ?> DING_TALK_COPR_ACTION_CARD_MULTI() {
        return com.regent.rpush.dto.message.dingtalk.corp.ActionCardMultiMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.class, MessageType.DING_TALK_COPR_ACTION_CARD_SINGLE);
    }
    /**
     * 钉钉-工作通知卡片-单按钮
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.ActionCardSingleMessageDTOBuilder<?, ?> DING_TALK_COPR_ACTION_CARD_SINGLE() {
        return com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.class, MessageType.DING_TALK_COPR_LINK);
    }
    /**
     * 钉钉-工作通知链接消息
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.LinkMessageDTOBuilder<?, ?> DING_TALK_COPR_LINK() {
        return com.regent.rpush.dto.message.dingtalk.corp.LinkMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.class, MessageType.DING_TALK_COPR_MARKDOWN);
    }
    /**
     * 钉钉-工作通知Markdown
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> DING_TALK_COPR_MARKDOWN() {
        return com.regent.rpush.dto.message.dingtalk.corp.MarkdownMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.class, MessageType.DING_TALK_COPR_OA);
    }
    /**
     * 钉钉-工作通知OA消息
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.OaMessageDTOBuilder<?, ?> DING_TALK_COPR_OA() {
        return com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.class, MessageType.DING_TALK_COPR_TEXT);
    }
    /**
     * 钉钉-工作通知文本
     */
    public static com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.TextMessageDTOBuilder<?, ?> DING_TALK_COPR_TEXT() {
        return com.regent.rpush.dto.message.dingtalk.corp.TextMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_NEWS);
    }
    /**
     * 微信公众号图文消息
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_NEWS() {
        return com.regent.rpush.dto.message.wechatofficialaccount.NewsMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_TEXT);
    }
    /**
     * 微信公众号文本
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_TEXT() {
        return com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.class, MessageType.WECHAT_OFFICIAL_ACCOUNT_TEMPLATE);
    }
    /**
     * 微信公众号模板消息
     */
    public static com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.TemplateMessageDTOBuilder<?, ?> WECHAT_OFFICIAL_ACCOUNT_TEMPLATE() {
        return com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.class, MessageType.WECHAT_WORK_AGENT_FILE);
    }
    /**
     * 企业微信-应用消息文件
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.MediaMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_FILE() {
        return com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.class, MessageType.WECHAT_WORK_AGENT_IMAGE);
    }
    /**
     * 企业微信-应用消息图片
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.MediaMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_IMAGE() {
        return com.regent.rpush.dto.message.wechatwork.agent.MediaMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.class, MessageType.WECHAT_WORK_AGENT_MARKDOWN);
    }
    /**
     * 企业微信-应用消息Markdown
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_MARKDOWN() {
        return com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.class, MessageType.WECHAT_WORK_AGENT_NEWS);
    }
    /**
     * 企业微信-应用消息图文消息
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_NEWS() {
        return com.regent.rpush.dto.message.wechatwork.agent.NewsMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.class, MessageType.WECHAT_WORK_AGENT_TEXTCARD);
    }
    /**
     * 企业微信-应用消息文本卡片
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.TextCardMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_TEXTCARD() {
        return com.regent.rpush.dto.message.wechatwork.agent.TextCardMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.class, MessageType.WECHAT_WORK_AGENT_TEXT);
    }
    /**
     * 企业微信-应用消息文本
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_TEXT() {
        return com.regent.rpush.dto.message.wechatwork.agent.TextMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.class, MessageType.WECHAT_WORK_AGENT_VIDEO);
    }
    /**
     * 企业微信-应用消息视频
     */
    public static com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.VideoMessageDTOBuilder<?, ?> WECHAT_WORK_AGENT_VIDEO() {
        return com.regent.rpush.dto.message.wechatwork.agent.VideoMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_IMAGE);
    }
    /**
     * 企业微信-群机器人图片
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.ImageMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_IMAGE() {
        return com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_MARKDOWN);
    }
    /**
     * 企业微信-群机器人Markdown
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.MarkdownMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_MARKDOWN() {
        return com.regent.rpush.dto.message.wechatwork.robot.MarkdownMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_NEWS);
    }
    /**
     * 企业微信-群机器人图文消息
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.NewsMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_NEWS() {
        return com.regent.rpush.dto.message.wechatwork.robot.NewsMessageDTO.builder();
    }

    static {
        MESSAGE_TYPE_PARAM_MAP.put(com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.class, MessageType.WECHAT_WORK_ROBOT_TEXT);
    }
    /**
     * 企业微信-群机器人文本
     */
    public static com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.TextMessageDTOBuilder<?, ?> WECHAT_WORK_ROBOT_TEXT() {
        return com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO.builder();
    }

}
