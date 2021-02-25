package com.regent.rpush.server.utils;

import org.springframework.stereotype.Component;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 17:39
 **/
@Component
public class SpringConfig {

    public static final String HEARTBEAT_TIME = "rpush.server.heartBeatTime";

    public static String get(String key) {
        return SpringBeanFactory.getContext().getEnvironment().getProperty(key);
    }

    public static long getHeartbeatTime() {
        return Long.parseLong(get(HEARTBEAT_TIME));
    }

}
