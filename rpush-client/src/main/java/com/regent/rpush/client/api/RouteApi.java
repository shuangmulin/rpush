package com.regent.rpush.client.api;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.OfflineDTO;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 钟宝林
 * @since 2021/2/26/026 22:35
 **/
public class RouteApi {

    /**
     * 路由服务名称
     */
    private static final String RPUSH_ROUTE_SERVICE_NAME = "rpush-route";

    /**
     * 获取服务端信息
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public static ApiResult<ServerInfoDTO> route(String servicePath) {
        String body = HttpUtil.get(servicePath + "/" + RPUSH_ROUTE_SERVICE_NAME + "/route");
        return parseResult(body, "获取路由信息失败")
                .toBean(new TypeReference<ApiResult<ServerInfoDTO>>() {
                });
    }

    /**
     * 下线
     */
    public static ApiResult<String> offline(String servicePath, long registrationId) {
        OfflineDTO offlineDTO = OfflineDTO.builder().registrationId(registrationId).build();
        String body = HttpUtil.post(servicePath + "/" + RPUSH_ROUTE_SERVICE_NAME + "/rpush-server-online/offline", JSONUtil.toJsonStr(offlineDTO));
        return parseResult(body, null)
                .toBean(new TypeReference<ApiResult<String>>() {
                });
    }

    private static JSONObject parseResult(String resultBody, String errorMsg) {
        try {
            return new JSONObject(resultBody);
        } catch (JSONException e) {
            errorMsg = StringUtils.isBlank(errorMsg) ? "请求失败" : errorMsg;
            throw new IllegalStateException(errorMsg + "，" + resultBody);
        }
    }
}
