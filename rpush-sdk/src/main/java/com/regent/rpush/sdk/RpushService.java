package com.regent.rpush.sdk;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.regent.rpush.dto.StatusCode;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.rpushserver.PageOnlineDTO;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import com.regent.rpush.dto.table.Pagination;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Rpush消息sdk
 *
 * @author 钟宝林
 * @since 2021/5/18/018 18:51
 **/
public class RpushService {

    /**
     * 默认rpush服务路径
     */
    private static final String DEFAULT_BASE_URL = "http://159.75.121.163";
    /**
     * 路由服务名称
     */
    private static final String RPUSH_ROUTE_SERVICE_NAME = "rpush-route";

    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private static final String GRANT_TYPE_PASSWORD = "password";

    private final String baseUrl;
    /**
     * 授权方式，client_credentials或者password（如果为空，则认为是外部维护的accessToken）
     */
    private String grantType;
    /**
     * 获取accessToke的url
     */
    private String accessTokenUrl;

    public static TimedCache<String, String> accessTokenTimedCache;

    /**
     * 如果由外部程序维护accessToken，可以用这个方法构造投递器
     *
     * @param baseUrl Rpush服务路径，http://ip:port
     */
    public static RpushService instance(String baseUrl) {
        return Singleton.get(RpushService.class, baseUrl);
    }

    public static RpushService instance() {
        return Singleton.get(RpushService.class);
    }

    /**
     * 如果是client_credentials的授权方式，用这个方法构造消息sdk
     *
     * @param baseUrl      Rpush服务路径，http://ip:port
     * @param clientId     clientId
     * @param clientSecret clientSecret
     */
    public static RpushService instance(String baseUrl, String clientId, String clientSecret) {
        return Singleton.get(RpushService.class, baseUrl, clientId, clientSecret);
    }

    public static RpushService instance(String clientId, String clientSecret) {
        return Singleton.get(RpushService.class, clientId, clientSecret);
    }

    /**
     * 如果是password的授权方式，用这个方法构造消息sdk
     *
     * @param baseUrl      Rpush服务路径，http://ip:port
     * @param clientId     clientId
     * @param clientSecret clientSecret
     * @param loginName    loginName
     * @param password     password
     */
    public static RpushService instance(String baseUrl, String clientId, String clientSecret, String loginName, String password) {
        return Singleton.get(RpushService.class, baseUrl, clientId, clientSecret, loginName, password);
    }

    public static RpushService instance(String clientId, String clientSecret, String loginName, String password) {
        return Singleton.get(RpushService.class, clientId, clientSecret, loginName, password);
    }

    private RpushService() {
        this(DEFAULT_BASE_URL);
    }

    private RpushService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private RpushService(String clientId, String clientSecret) {
        this(DEFAULT_BASE_URL, clientId, clientSecret);
    }

    private RpushService(String baseUrl, String clientId, String clientSecret) {
        this.baseUrl = baseUrl;

        this.grantType = GRANT_TYPE_CLIENT_CREDENTIALS;
        this.accessTokenUrl = baseUrl + "/" + RPUSH_ROUTE_SERVICE_NAME + "/oauth/token?grant_type=" + grantType + "&scope=all&client_id=" + clientId + "&client_secret=" + clientSecret;
    }

    private RpushService(String baseUrl, String clientId, String clientSecret, String loginName, String password) {
        this.baseUrl = baseUrl;

        this.grantType = GRANT_TYPE_PASSWORD;
        this.accessTokenUrl = baseUrl + "/" + RPUSH_ROUTE_SERVICE_NAME + "/oauth/token?grant_type=" + grantType + "&scope=all&client_id="
                + clientId + "&client_secret=" + clientSecret
                + "&username=" + loginName + "&password=" + password;
    }

    private RpushService(String clientId, String clientSecret, String loginName, String password) {
        this(DEFAULT_BASE_URL, clientId, clientSecret, loginName, password);
    }

    private TimedCache<String, String> getAccessTokenTimedCache() {
        if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
            synchronized (RpushService.class) {
                if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
                    String result = HttpRequest.post(accessTokenUrl)
                            .header("Accept", "application/json")
                            .timeout(30000)//超时，毫秒
                            .execute().body();
                    JSONObject jsonObject = parseResult(result);
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
        if (StringUtils.isBlank(grantType)) {
            // 没有指定授权方式
            throw new IllegalStateException("获取token的参数不全");
        }
        return getAccessTokenTimedCache().get("accessToken");
    }

    private void checkToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("没有accessToken，无法发起请求");
        }
    }

    /**
     * 投递消息
     *
     * @param messages 需要投递的消息
     * @return 直接返回json字符串
     */
    public String sendMessage(BaseMessage... messages) {
        return sendMessage(getAccessToken(), messages);
    }

    /**
     * 投递消息
     *
     * @param accessToken accessToken
     * @param messages    需要投递的消息
     * @return 直接返回json字符串
     */
    public String sendMessage(String accessToken, BaseMessage... messages) {
        checkToken(accessToken);

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
        body.set("messageParam", params);

        return HttpRequest.post(baseUrl + "/" + RPUSH_ROUTE_SERVICE_NAME + "/message/push")
                .header("Authorization", "Bearer " + accessToken)
                .body(body.toString())
                .timeout(30000)//超时，毫秒
                .execute().body();
    }

    /**
     * 获取所有在线的设备信息
     *
     * @param param 参数
     * @return 分页数据
     */
    public Pagination<RpushServerRegistrationDTO> pageOnlineRegistrations(PageOnlineDTO param) {
        return pageOnlineRegistrations(getAccessToken(), param);
    }

    /**
     * 获取所有在线的设备信息
     *
     * @param accessToken accessToken
     * @param param       参数
     * @return 分页数据
     */
    public Pagination<RpushServerRegistrationDTO> pageOnlineRegistrations(String accessToken, PageOnlineDTO param) {
        checkToken(accessToken);

        String body = HttpRequest.get(baseUrl + "/" + RPUSH_ROUTE_SERVICE_NAME + "/rpush-server-online/registrations")
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(JSONUtil.toJsonStr(param))
                .timeout(30000)//超时，毫秒
                .execute().body();

        JSONObject result = parseResult(body);
        if (result.getInt("code") != StatusCode.SUCCESS.getCode()) {
            throw new IllegalStateException("请求失败，" + result.getStr("msg"));
        }
        return result.getJSONObject("data").toBean(new TypeReference<Pagination<RpushServerRegistrationDTO>>() {

        });
    }

    /**
     * 获取某个设备的信息
     *
     * @param accessToken    accessToken
     * @param registrationId registrationId
     */
    public RpushServerRegistrationDTO getRegistration(String accessToken, String registrationId) {
        checkToken(accessToken);

        String body = HttpRequest.get(baseUrl + "/" + RPUSH_ROUTE_SERVICE_NAME + "/rpush-server-registration/" + registrationId)
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .timeout(30000)//超时，毫秒
                .execute().body();

        JSONObject result = parseResult(body);
        if (result.getInt("code") != StatusCode.SUCCESS.getCode()) {
            throw new IllegalStateException("请求失败，" + result.getStr("msg"));
        }
        return result.getBean("data", RpushServerRegistrationDTO.class);
    }

    /**
     * 获取某个设备的信息
     *
     * @param registrationId registrationId
     */
    public RpushServerRegistrationDTO getRegistration(String registrationId) {
        return getRegistration(getAccessToken(), registrationId);
    }

    private JSONObject parseResult(String resultBody) {
        try {
            return new JSONObject(resultBody);
        } catch (JSONException e) {
            throw new IllegalStateException("请求失败，" + resultBody);
        }
    }

}
