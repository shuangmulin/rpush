package com.regent.rpush.server.socket.session;

import com.regent.rpush.server.socket.RpushClient;

/**
 * 一个Socket会话
 *
 * @author 钟宝林
 * @date 2021/2/11/011 14:39
 **/
public interface SocketSession {

    /**
     * 一个注册id一个Socket，一个Socket一个Session
     */
    Long getRegistrationId();

    RpushClient getClient();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

}
