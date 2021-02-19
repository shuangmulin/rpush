package com.regent.rpush.server.socket;

import io.netty.channel.socket.nio.NioSocketChannel;

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
    private NioSocketChannel nioSocketChannel;
    private final Map<String, Object> attributeMap = new ConcurrentHashMap<>();

    public SocketSessionImpl(NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
    }

    @Override
    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public NioSocketChannel getNioSocketChannel() {
        return nioSocketChannel;
    }

    public void setNioSocketChannel(NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
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
