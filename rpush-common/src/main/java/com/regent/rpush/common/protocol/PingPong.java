package com.regent.rpush.common.protocol;

import com.regent.rpush.common.Constants;
import com.regent.rpush.common.SingletonUtil;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 22:20
 **/
public class PingPong {

    public static MessageProto.MessageProtocol ping() {
        return SingletonUtil.get("ping", () ->
                MessageProto.MessageProtocol.newBuilder()
                        .setType(Constants.MessageType.PING)
                        .setFromTo(-1)
                        .setSendTo(-1)
                        .setContent("ping")
                        .build());
    }

    public static MessageProto.MessageProtocol pong() {
        return SingletonUtil.get("pong", () ->
                MessageProto.MessageProtocol.newBuilder()
                        .setType(Constants.MessageType.PING)
                        .setFromTo(-1)
                        .setSendTo(-1)
                        .setContent("pong")
                        .build());
    }

}
