package com.regent.rpush.server.socket.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nio客户端
 *
 * @author 钟宝林
 * @since 2021/2/20/020 20:37
 **/
public class NioSocketChannelClient implements RpushClient {

    private static final Map<Channel, NioSocketChannelClient> INSTANCE_MAP = new ConcurrentHashMap<>();

    private final Channel channel;

    private NioSocketChannelClient(Channel channel) {
        this.channel = channel;
    }

    public static RpushClient getInstance(Channel nioSocketChannel) {
        NioSocketChannelClient client = INSTANCE_MAP.get(nioSocketChannel);
        if (client != null) {
            return client;
        }
        synchronized (NioSocketChannelClient.class) {
            if (INSTANCE_MAP.get(nioSocketChannel) == null) {
                INSTANCE_MAP.put(nioSocketChannel, new NioSocketChannelClient(nioSocketChannel));
            }
        }
        return INSTANCE_MAP.get(nioSocketChannel);
    }

    @Override
    public void close() throws Exception {
        if (channel.isOpen()) {
            channel.close();
        }
    }
}
