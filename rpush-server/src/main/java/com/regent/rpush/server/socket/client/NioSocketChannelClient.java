package com.regent.rpush.server.socket.client;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nio客户端
 *
 * @author 钟宝林
 * @since 2021/2/20/020 20:37
 **/
public class NioSocketChannelClient implements RpushClient {

    private static final Map<NioSocketChannel, NioSocketChannelClient> INSTANCE_MAP = new ConcurrentHashMap<>();

    private NioSocketChannel nioSocketChannel;

    private NioSocketChannelClient(NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
    }

    public static NioSocketChannelClient getInstance(NioSocketChannel nioSocketChannel) {
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

}
