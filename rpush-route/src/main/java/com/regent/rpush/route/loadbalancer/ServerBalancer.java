package com.regent.rpush.route.loadbalancer;

import com.alibaba.druid.util.StringUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import java.util.List;

public class ServerBalancer extends ZoneAvoidanceRule {

    @Override
    public Server choose(Object o) {
        try {
            String serverId = MessageRequestInterceptor.SERVER_ID.get();
            if (StringUtils.isEmpty(serverId)) {
                return super.choose(o);
            }
            List<Server> servers = getLoadBalancer().getAllServers();
            for (Server server : servers) {
                if (StringUtils.equals(server.getId(), serverId)) {
                    return server;
                }
            }
            throw new IllegalArgumentException("用户未登录");
        } finally {
            MessageRequestInterceptor.SERVER_ID.remove();
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
