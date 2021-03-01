package com.regent.rpush.route.loadbalancer;

import com.alibaba.druid.util.StringUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import java.util.List;

/**
 * 路由->Socket服务端请求的实例选择
 */
public class ServerBalancer extends ZoneAvoidanceRule {

    @Override
    public Server choose(Object o) {
        try {
            // 看有没有指定服务端实例
            String serverId = MessageRequestInterceptor.SERVER_ID.get();
            if (StringUtils.isEmpty(serverId)) {
                // 如果没有指定服务端实例，用默认的负载均衡算法
                return super.choose(o);
            }
            // 如果指定了服务端实例，用指定的实例发请求
            List<Server> servers = getLoadBalancer().getAllServers();
            for (Server server : servers) {
                if (StringUtils.equals(server.getId(), serverId)) {
                    return server;
                }
            }
            throw new IllegalArgumentException("没有可用的RPUSH_SERVER实例");
        } finally {
            MessageRequestInterceptor.SERVER_ID.remove();
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
