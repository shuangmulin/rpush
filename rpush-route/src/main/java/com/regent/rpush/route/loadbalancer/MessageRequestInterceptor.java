package com.regent.rpush.route.loadbalancer;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MessageRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.url();
        String method = requestTemplate.method();
        if (!"/message/push".equals(url) || !"GET".equals(method)) {
            return;
        }

        String s = Arrays.toString(requestTemplate.body());
        System.out.println(s);
    }

}
