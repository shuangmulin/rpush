package com.regent.rpush.server.socket.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.regent.rpush.dto.message.NormalMessageDTO;
import com.regent.rpush.server.socket.RpushClient;
import com.regent.rpush.server.socket.session.SocketSessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageEventHandler {

    /**
     * 客户端连接的时候触发
     */
    @OnConnect
    public void onConnect(SocketIOClient socketIOClient) {
        RpushClient client = WebSocketClient.getInstance(socketIOClient);
        String registrationIdStr = socketIOClient.getHandshakeData().getSingleUrlParam("registrationId");
        if (StringUtils.isBlank(registrationIdStr) && !StringUtils.isNumeric(registrationIdStr)) {
            client.pushMessage(NormalMessageDTO.builder().content("未知用户").build());
            client.close();
            return;
        }
        long registrationId = Long.parseLong(registrationIdStr);
        SocketSessionHolder.login(registrationId, client);

        // 回发连接成功消息
        client.pushMessage(NormalMessageDTO.builder().content("连接成功").build());
        log.info("客户端:" + registrationId + "已连接");
    }

    /**
     * 客户端关闭连接时触发
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        long registrationId = SocketSessionHolder.offLine(WebSocketClient.getInstance(client));
        if (registrationId > 0) {
            log.info("客户端:" + registrationId + "断开连接");
        }
    }

    /**
     * 客户端事件
     *
     * @param client  　客户端信息
     * @param request 请求信息
     * @param data    　客户端发送数据
     */
    @OnEvent(value = "messageEvent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        client.sendEvent("messageEvent", NormalMessageDTO.builder().content("pong").build());
    }
}