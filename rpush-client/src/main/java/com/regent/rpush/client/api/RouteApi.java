package com.regent.rpush.client.api;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.regent.rpush.client.Config;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.OfflineDTO;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;

/**
 * @author 钟宝林
 * @since 2021/2/26/026 22:35
 **/
public class RouteApi {

    private static final String BASE_URL = "http://" + Config.getRouteHost() + ":" + Config.getRoutePort();

    /**
     * 获取服务端信息
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public static ApiResult<ServerInfoDTO> route() {
        try {
            String body = HttpUtil.get(BASE_URL + "/route");
            return JSONUtil.parseObj(body)
                    .toBean(new TypeReference<ApiResult<ServerInfoDTO>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("获取路由信息失败");
        }
    }

    /**
     * 下线
     */
    public static ApiResult<String> offline() {
        OfflineDTO offlineDTO = OfflineDTO.builder().registrationId(Config.getRegistrationId()).build();
        String body = HttpUtil.post(BASE_URL + "/rpush-server-online/offline", JSONUtil.toJsonStr(offlineDTO));
        return JSONUtil.parseObj(body)
                .toBean(new TypeReference<ApiResult<String>>() {
                });
    }
}
