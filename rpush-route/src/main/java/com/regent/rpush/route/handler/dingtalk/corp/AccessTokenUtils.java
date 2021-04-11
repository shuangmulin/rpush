package com.regent.rpush.route.handler.dingtalk.corp;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 钟宝林
 * @since 2021/4/11/011 15:13
 **/
public class AccessTokenUtils {

    public static TimedCache<String, String> accessTokenTimedCache;

    private static TimedCache<String, String> getAccessTokenTimedCache(String appKey, String appSecret) throws ApiException {
        if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
            synchronized (AccessTokenUtils.class) {
                if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
                    DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
                    OapiGettokenRequest request = new OapiGettokenRequest();
                    request.setAppkey(appKey);
                    request.setAppsecret(appSecret);
                    request.setHttpMethod("GET");
                    OapiGettokenResponse response;
                    response = client.execute(request);
                    accessTokenTimedCache = CacheUtil.newTimedCache((response.getExpiresIn() - 60) * 1000);
                    accessTokenTimedCache.put("accessToken", response.getAccessToken());
                }
            }
        }
        return accessTokenTimedCache;
    }

    public static String getAccessToken(String appKey, String appSecret) throws ApiException {
        return getAccessTokenTimedCache(appKey, appSecret).get("accessToken");
//        return "201676988b0434baa49894ed8c861139";
    }

}
