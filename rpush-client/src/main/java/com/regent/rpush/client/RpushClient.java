package com.regent.rpush.client;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.regent.rpush.client.api.RouteApi;
import com.regent.rpush.common.Constants;
import com.regent.rpush.common.protocol.MessageProto;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RpushClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpushClient.class);

    /**
     * 路由服务路径
     */
    private final String servicePath;
    /**
     * 设备id
     */
    private final long registrationId;
    /**
     * 消息处理器
     */
    private final List<MsgProcessor> msgProcessors = new ArrayList<>();

    /**
     * 添加消息处理器，如果有消息接收到，会按顺序触发
     *
     * @param msgProcessor 消息处理器
     */
    public void addMsgProcessor(MsgProcessor msgProcessor) {
        msgProcessors.add(msgProcessor);
        msgProcessors.sort(Comparator.comparingInt(MsgProcessor::getOrder));
    }

    List<MsgProcessor> getMsgProcessors() {
        return msgProcessors;
    }

    public RpushClient(String servicePath, long registrationId) {
        this.servicePath = servicePath;
        this.registrationId = registrationId;
    }

    private final EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("rpush-work"));

    private SocketChannel channel;

    /**
     * 重试次数
     */
    private int errorCount;
    /**
     * 重连执行器
     */
    private ScheduledExecutorService reconnectExecutor;


    public void start() {
        // 获取可以使用的服务器 ip+port
        ServerInfoDTO serverInfoDTO = RouteApi.route(servicePath).getData();

        // 启动客户端
        startClient(serverInfoDTO);

        // 向服务端发送登录请求
        login();
    }

    /**
     * 向服务端发送登录
     */
    private void login() {
        MessageProto.MessageProtocol login = MessageProto.MessageProtocol.newBuilder()
                .setSendTo(-1)
                .setFromTo(registrationId)
                .setContent("login")
                .setType(Constants.MessageType.LOGIN)
                .build();
        ChannelFuture future = channel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("登录成功")
        );
    }

    /**
     * 启动客户端
     */
    private void startClient(ServerInfoDTO server) {
        RpushClient rpushClient = this;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                                .addLast(new IdleStateHandler(0, 10, 0))
                                // google Protobuf 编解码
                                .addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(new RpushClientHandler(rpushClient))
                        ;
                    }
                })
        ;

        ChannelFuture future = null;
        try {
            future = bootstrap.connect(server.getHost(), server.getSocketPort()).sync();
        } catch (Exception e) {
            errorCount++;
            if (errorCount >= 10) {
                LOGGER.info("连接失败次数达到上限[" + errorCount + "]次");
                System.exit(0);
            }
        }
        if (future.isSuccess()) {
            LOGGER.info("服务器连接成功!");
        }
        channel = (SocketChannel) future.channel();
    }

    void reconnect() {
        ScheduledExecutorService reconnectExecutor = getReconnectExecutor();
        reconnectExecutor.scheduleAtFixedRate(() -> {
            if (channel != null && channel.isActive()) {
                return;
            }
            try {
                // 首先清除路由信息，下线
                RouteApi.offline(servicePath, registrationId);

                LOGGER.info("服务器断连, 尝试重连中....");
                start();
                LOGGER.info("重连成功!!!");
                reconnectExecutor.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.warn("重连服务端失败");
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private ScheduledExecutorService getReconnectExecutor() {
        if (reconnectExecutor == null || reconnectExecutor.isShutdown()) {
            ThreadFactory sche = new ThreadFactoryBuilder()
                    .setNamePrefix("reConnect-job-%d")
                    .setDaemon(true)
                    .build();
            reconnectExecutor = new ScheduledThreadPoolExecutor(1, sche);
        }
        return reconnectExecutor;
    }

    public long getRegistrationId() {
        return registrationId;
    }
}
