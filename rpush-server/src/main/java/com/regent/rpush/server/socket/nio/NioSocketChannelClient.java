package com.regent.rpush.server.socket.nio;

import com.regent.rpush.common.Constants;
import com.regent.rpush.common.protocol.MessageProto;
import com.regent.rpush.dto.message.NormalMessageDTO;
import com.regent.rpush.server.socket.RpushClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nio客户端
 *
 * @author 钟宝林
 * @since 2021/2/20/020 20:37
 **/
public class NioSocketChannelClient implements RpushClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(NioSocketChannelClient.class);

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
    public void close() {
        if (channel.isOpen()) {
            channel.close();
        }
    }

    @Override
    public void pushMessage(NormalMessageDTO message) {
        MessageProto.MessageProtocol messageProtocol = MessageProto.MessageProtocol.newBuilder()
                .setContent(message.getContent())
                .setFromTo(message.getFromTo())
                .setSendTo(message.getSendTo())
                .setType(Constants.MessageType.MSG)
                .build();
        ChannelFuture future = channel.writeAndFlush(messageProtocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("server push msg:[{}]", message.toString()));
    }
}
