package com.regent.rpush.scheduler.route;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 钟宝林
 * @since 2021/4/11/011 15:13
 **/
@Component
public class RouteAccessTokenService {

    public static TimedCache<String, String> accessTokenTimedCache;

    @Autowired
    private RestTemplate restTemplate;

    private TimedCache<String, String> getAccessTokenTimedCache() {
        if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
            synchronized (RouteAccessTokenService.class) {
                if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get("accessToken"))) {
                    ResponseEntity<String> forEntity = restTemplate.getForEntity("http://rpush-route/oauth/token?grant_type=client_credentials&scope=all&client_id=scheduler&client_secret=scheduler123", String.class);
                    JSONObject jsonObject = new JSONObject(forEntity.getBody());
                    Long expiresIn = jsonObject.getLong("expires_in");
                    String accessToken = jsonObject.getStr("access_token");
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

}
