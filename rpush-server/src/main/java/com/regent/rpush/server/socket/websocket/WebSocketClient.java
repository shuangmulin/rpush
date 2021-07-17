package com.regent.rpush.server.socket.websocket;

import cn.hutool.json.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.regent.rpush.dto.message.NormalMessageDTO;
import com.regent.rpush.server.socket.RpushClient;
import com.regent.rpush.server.socket.nio.NioSocketChannelClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket客户端实现
 *
 * @author 钟宝林
 * @since 2021/3/2/002 18:52
 **/
public class WebSocketClient implements RpushClient {

    private static final Map<SocketIOClient, WebSocketClient> INSTANCE_MAP = new ConcurrentHashMap<>();

    private final SocketIOClient socketIOClient;

    private WebSocketClient(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    public static RpushClient getInstance(SocketIOClient socketIOClient) {
        WebSocketClient client = INSTANCE_MAP.get(socketIOClient);
        if (client != null) {
            return client;
        }
        synchronized (NioSocketChannelClient.class) {
            if (INSTANCE_MAP.get(socketIOClient) == null) {
                INSTANCE_MAP.put(socketIOClient, new WebSocketClient(socketIOClient));
            }
        }
        return INSTANCE_MAP.get(socketIOClient);
    }

    @Override
    public void pushMessage(NormalMessageDTO message) {
        JSONObject jsonObject = new JSONObject(message);
        jsonObject.set("sendTo", jsonObject.getStr("sendTo"));
        jsonObject.set("fromTo", jsonObject.getStr("fromTo"));
        socketIOClient.sendEvent("message", jsonObject.toString());
    }

    @Override
    public void close() {
        socketIOClient.disconnect();
    }
}
