package com.regent.rpush.server.socket.session;

import com.regent.rpush.server.socket.RpushClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SocketSession默认实现
 *
 * @author 钟宝林
 * @date 2021/2/11/011 20:38
 **/
public class SocketSessionImpl implements SocketSession {

    private Long registrationId;
    private RpushClient client;
    private final Map<String, Object> attributeMap = new ConcurrentHashMap<>();

    public SocketSessionImpl(RpushClient client) {
        this.client = client;
    }

    @Override
    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public RpushClient getClient() {
        return client;
    }

    public void setClient(RpushClient Client) {
        this.client = Client;
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }
}
