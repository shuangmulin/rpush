package com.regent.rpush.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RpushClient {

    /**
     * 路由服务路径
     */
    private final String routeServicePath;
    /**
     * 设备id
     */
    private final long registrationId;

    public RpushClient(String routeServicePath, long registrationId) {
        this.routeServicePath = routeServicePath;
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
        ServerInfoDTO serverInfoDTO = RouteApi.route(routeServicePath).getData();

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
                System.out.println("登录成功")
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
                System.out.println("连接失败次数达到上限[" + errorCount + "]次");
                System.exit(0);
            }
        }
        if (future.isSuccess()) {
            System.out.println("Start cim client success!");
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
                RouteApi.offline(routeServicePath, registrationId);

                System.out.println("服务器断连, 尝试重连中....");
                start();
                System.out.println("重连成功!!!");
                reconnectExecutor.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("重连服务端失败");
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private ScheduledExecutorService getReconnectExecutor() {
        if (reconnectExecutor == null || reconnectExecutor.isShutdown()) {
            ThreadFactory sche = new ThreadFactoryBuilder()
                    .setNameFormat("reConnect-job-%d")
                    .setDaemon(true)
                    .build();
            reconnectExecutor = new ScheduledThreadPoolExecutor(1, sche);
        }
        return reconnectExecutor;
    }

}
