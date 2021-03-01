package com.regent.rpush.route.loadbalancer;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rpush.route.model.RpushServerOnline;
import com.regent.rpush.route.service.IRpushServerOnlineService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageRequestInterceptor implements RequestInterceptor {

    static final ThreadLocal<String> SERVER_ID = new ThreadLocal<>();

    @Autowired
    private IRpushServerOnlineService rpushServerOnlineService;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.url();
        String method = requestTemplate.method();
        if (!"/push".equals(url) || !"POST".equals(method)) {
            return;
        }

        // 如果是消息推送，需要给接收端连接的服务端投放消息，在服务端集群的情况下，要找到对应的服务端
        String body = new String(requestTemplate.body());
        JSONObject jsonObject = new JSONObject(body);
        QueryWrapper<RpushServerOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("registration_id", jsonObject.getStr("sendTo"));
        RpushServerOnline rpushServerOnline = rpushServerOnlineService.getOne(wrapper);
        String serverId = rpushServerOnline.getServerId();
        SERVER_ID.set(serverId);
    }

}
