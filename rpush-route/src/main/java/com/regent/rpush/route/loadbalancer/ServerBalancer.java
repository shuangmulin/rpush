package com.regent.rpush.route.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class ServerBalancer extends AbstractLoadBalancerRule {

    @Override
    public Server choose(Object o) {
        List<Server> servers = getLoadBalancer().getAllServers();
        for (Server server : servers) {
            System.out.println(server.getHostPort());
        }
        return servers.get(0);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
