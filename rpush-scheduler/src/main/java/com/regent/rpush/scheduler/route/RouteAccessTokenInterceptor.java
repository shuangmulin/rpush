package com.regent.rpush.scheduler.route;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteAccessTokenInterceptor implements RequestInterceptor {

    @Autowired
    private RouteAccessTokenService routeAccessTokenService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Target<?> target = requestTemplate.feignTarget();
        if (StringUtils.equals(target.name(), "rpush-route")) {
            String accessToken = routeAccessTokenService.getAccessToken();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        }
    }

}
