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
            MessageType messageType = RpushMessage.MESSAGE_TYPE_PARAM_MAP.get(message.getClass());
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

}
