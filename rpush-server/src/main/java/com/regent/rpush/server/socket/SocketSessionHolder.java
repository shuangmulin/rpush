package com.regent.rpush.server.socket;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket登录信息
 *
 * @author 钟宝林
 * @date 2021/2/11/011 14:32
 **/
public final class SocketSessionHolder {

    /**
     * session持有
     */
    private static final Map<NioSocketChannel, SocketSession> CHANNEL_SESSION_MAP = new ConcurrentHashMap<>();
    private static final Map<Long, SocketSession> REGISTRATION_ID_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 根据通道添加session
     */
    public static void add(NioSocketChannel nioSocketChannel) {
        SocketSession socketSession = CHANNEL_SESSION_MAP.get(nioSocketChannel);
        if (socketSession == null) {
            socketSession = new SocketSessionImpl(nioSocketChannel);
            CHANNEL_SESSION_MAP.put(nioSocketChannel, socketSession);
        }
    }

    /**
     * 根据通道获取session
     */
    public static SocketSession get(NioSocketChannel nioSocketChannel) {
        add(nioSocketChannel);
        return CHANNEL_SESSION_MAP.get(nioSocketChannel);
    }

    /**
     * 登录
     */
    public static void login(Long registrationId, NioSocketChannel nioSocketChannel) {
        SocketSessionImpl socketSession = (SocketSessionImpl) get(nioSocketChannel);
        socketSession.setRegistrationId(registrationId);
        REGISTRATION_ID_SESSION_MAP.put(registrationId, socketSession);
    }

    /**
     * 根据注册id获取session，如果没有登录，会返回null
     */
    public static SocketSession getSocketSession(Long registrationId) {
        return REGISTRATION_ID_SESSION_MAP.get(registrationId);
    }

    public static void offLine(Long registrationId) {
        SocketSession socketSession = getSocketSession(registrationId);
        if (socketSession == null) {
            // 没登录过，不管
            return;
        }
        CHANNEL_SESSION_MAP.remove(socketSession.getNioSocketChannel());
        REGISTRATION_ID_SESSION_MAP.remove(registrationId);
    }

    public static void offLine(NioSocketChannel nioSocketChannel) {
        SocketSession socketSession = get(nioSocketChannel);
        if (socketSession == null) {
            return;
        }

        Long registrationId = socketSession.getRegistrationId();
        if (registrationId != null) {
            REGISTRATION_ID_SESSION_MAP.remove(registrationId);
        }

        CHANNEL_SESSION_MAP.remove(nioSocketChannel);
    }

}
