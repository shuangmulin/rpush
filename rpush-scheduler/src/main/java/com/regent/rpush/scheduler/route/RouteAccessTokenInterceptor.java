package com.regent.rpush.scheduler.route;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RouteAccessTokenInterceptor implements RequestInterceptor {

    @Autowired
    private RouteAccessTokenService routeAccessTokenService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Target<?> target = requestTemplate.feignTarget();
        String path = requestTemplate.path();
        String method = requestTemplate.method();
        if (StringUtils.equals(target.name(), "rpush-route")
                && StringUtils.equals(path, "/message/push")
                && StringUtils.equalsIgnoreCase(method, "post")) {
            // 消息发送，按scheduler授权
            String accessToken = routeAccessTokenService.getAccessToken();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
            return;
        }

        Map<String, String> headers = getHeaders();
        String authorization = headers.get("authorization");
        if (StringUtils.isBlank(authorization)) {
            String accessToken = routeAccessTokenService.getAccessToken();
            authorization = "Bearer " + accessToken;
        }
        requestTemplate.header("Authorization", authorization);
    }

    private Map<String, String> getHeaders() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new HashMap<>();
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
