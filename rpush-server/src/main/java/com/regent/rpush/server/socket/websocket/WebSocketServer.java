package com.regent.rpush.server.socket.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 钟宝林
 * @since 2021/3/2/002 19:20
 **/
@Component
@Slf4j
public class WebSocketServer implements CommandLineRunner {

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) {
        socketIOServer.start();
        log.info("WebSocket服务端启动成功！");
    }
}
