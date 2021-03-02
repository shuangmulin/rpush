package com.regent.rpush.server.socket.session;

import com.regent.rpush.api.route.RpushServerOnlineService;
import com.regent.rpush.dto.rpushserver.LoginDTO;
import com.regent.rpush.dto.rpushserver.OfflineDTO;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import com.regent.rpush.server.socket.RpushClient;
import com.regent.rpush.server.utils.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket登录信息
 *
 * @author 钟宝林
 * @date 2021/2/11/011 14:32
 **/
public final class SocketSessionHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(SocketSessionHolder.class);

    /**
     * session持有
     */
    private static final Map<RpushClient, SocketSession> CHANNEL_SESSION_MAP = new ConcurrentHashMap<>();
    private static final Map<Long, SocketSession> REGISTRATION_ID_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 根据通道添加session
     */
    public static void add(RpushClient client) {
        SocketSession socketSession = CHANNEL_SESSION_MAP.get(client);
        if (socketSession == null) {
            socketSession = new SocketSessionImpl(client);
            CHANNEL_SESSION_MAP.put(client, socketSession);
        }
    }

    /**
     * 根据通道获取session
     */
    public static SocketSession get(RpushClient Client) {
        add(Client);
        return CHANNEL_SESSION_MAP.get(Client);
    }

    /**
     * 登录
     */
    public static void login(Long registrationId, RpushClient Client) {
        // 建立SocketSession
        SocketSessionImpl socketSession = (SocketSessionImpl) get(Client);
        socketSession.setRegistrationId(registrationId);
        REGISTRATION_ID_SESSION_MAP.put(registrationId, socketSession);

        // 持久化登录信息
        RpushServerOnlineService rpushServerOnlineService = SpringBeanFactory.getBean(RpushServerOnlineService.class);
        ServerInfoDTO serverInfo = SpringBeanFactory.getBean(ServerInfoDTO.class);
        rpushServerOnlineService.login(LoginDTO.builder().registrationId(registrationId).serverInfo(serverInfo).build());
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
        offLine(socketSession.getClient());
    }

    public static long offLine(RpushClient client) {
        SocketSession socketSession = get(client);
        if (socketSession == null) {
            // 没登录过，不管
            return -1;
        }

        Long registrationId = socketSession.getRegistrationId();
        if (registrationId != null) {
            REGISTRATION_ID_SESSION_MAP.remove(registrationId);

            // 清除登录信息
            RpushServerOnlineService rpushServerOnlineService = SpringBeanFactory.getBean(RpushServerOnlineService.class);
            rpushServerOnlineService.offline(OfflineDTO.builder().registrationId(registrationId).build());
        }

        CHANNEL_SESSION_MAP.remove(client);

        try {
            client.close();
        } catch (Exception e) {
            LOGGER.error("客户端[{}]关闭异常", registrationId);
        }

        return registrationId == null ? -1 : registrationId;
    }

}
