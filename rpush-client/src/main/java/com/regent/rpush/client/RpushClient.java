package com.regent.rpush.client;

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

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 22/05/2018 14:19
 * @since JDK 1.8
 */
public class RpushClient {
    private final EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("rpush-work"));

    private SocketChannel channel;

    /**
     * 重试次数
     */
    private int errorCount;

    public void start() throws Exception {
        // 获取可以使用的服务器 ip+port
        ServerInfoDTO serverInfoDTO = RouteApi.route().getData();

        // 启动客户端
        startClient(serverInfoDTO);

        // 向服务端发送登录
        login();
    }

    /**
     * 向服务端发送登录
     */
    private void login() {
        MessageProto.MessageProtocol login = MessageProto.MessageProtocol.newBuilder()
                .setSendTo(-1)
                .setFromTo(Config.getRegistrationId())
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
                                //拆包解码
                                .addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(new RpushClientHandler())
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

//    private EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("rpush-work"));
//
//    private long userid;
//
//    private String userName;
//
//    private SocketChannel channel;
//
//    /**
//     * 重试次数
//     */
//    private int errorCount;
//
//    public void start() throws Exception {
//
//        //登录 + 获取可以使用的服务器 ip+port
//        CIMServerResVO.ServerInfo cimServer = userLogin();
//
//        //启动客户端
//        startClient(cimServer);
//
//        //向服务端注册
//        loginCIMServer();
//
//
//    }
//
//    /**
//     * 启动客户端
//     *
//     * @param cimServer
//     * @throws Exception
//     */
//    private void startClient(CIMServerResVO.ServerInfo cimServer) {
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .handler(new CIMClientHandleInitializer())
//        ;
//
//        ChannelFuture future = null;
//        try {
//            future = bootstrap.connect(cimServer.getIp(), cimServer.getCimServerPort()).sync();
//        } catch (Exception e) {
//            errorCount++;
//
//            if (errorCount >= configuration.getErrorCount()) {
//                LOGGER.error("连接失败次数达到上限[{}]次", errorCount);
//                msgHandle.shutdown();
//            }
//            LOGGER.error("Connect fail!", e);
//        }
//        if (future.isSuccess()) {
//            echoService.echo("Start cim client success!");
//            LOGGER.info("启动 cim client 成功");
//        }
//        channel = (SocketChannel) future.channel();
//    }
//
//    /**
//     * 登录+路由服务器
//     *
//     * @return 路由服务器信息
//     * @throws Exception
//     */
//    private CIMServerResVO.ServerInfo userLogin() {
//        LoginReqVO loginReqVO = new LoginReqVO(userId, userName);
//        CIMServerResVO.ServerInfo cimServer = null;
//        try {
//            cimServer = routeRequest.getCIMServer(loginReqVO);
//
//            //保存系统信息
//            clientInfo.saveServiceInfo(cimServer.getIp() + ":" + cimServer.getCimServerPort())
//                    .saveUserInfo(userId, userName);
//
//            LOGGER.info("cimServer=[{}]", cimServer.toString());
//        } catch (Exception e) {
//            errorCount++;
//
//            if (errorCount >= configuration.getErrorCount()) {
//                echoService.echo("The maximum number of reconnections has been reached[{}]times, close cim client!", errorCount);
//                msgHandle.shutdown();
//            }
//            LOGGER.error("login fail", e);
//        }
//        return cimServer;
//    }
//
//    /**
//     * 向服务器注册
//     */
//    private void loginCIMServer() {
//        CIMRequestProto.CIMReqProtocol login = CIMRequestProto.CIMReqProtocol.newBuilder()
//                .setRequestId(userId)
//                .setReqMsg(userName)
//                .setType(Constants.CommandType.LOGIN)
//                .build();
//        ChannelFuture future = channel.writeAndFlush(login);
//        future.addListener((ChannelFutureListener) channelFuture ->
//                        echoService.echo("Registry cim server success!")
//                );
//    }
//
//    /**
//     * 发送消息字符串
//     *
//     * @param msg
//     */
//    public void sendStringMsg(String msg) {
//        ByteBuf message = Unpooled.buffer(msg.getBytes().length);
//        message.writeBytes(msg.getBytes());
//        ChannelFuture future = channel.writeAndFlush(message);
//        future.addListener((ChannelFutureListener) channelFuture ->
//                LOGGER.info("客户端手动发消息成功={}", msg));
//
//    }
//
//    /**
//     * 发送 Google Protocol 编解码字符串
//     *
//     * @param googleProtocolVO
//     */
//    public void sendGoogleProtocolMsg(GoogleProtocolVO googleProtocolVO) {
//
//        CIMRequestProto.CIMReqProtocol protocol = CIMRequestProto.CIMReqProtocol.newBuilder()
//                .setRequestId(googleProtocolVO.getRequestId())
//                .setReqMsg(googleProtocolVO.getMsg())
//                .setType(Constants.CommandType.MSG)
//                .build();
//
//
//        ChannelFuture future = channel.writeAndFlush(protocol);
//        future.addListener((ChannelFutureListener) channelFuture ->
//                LOGGER.info("客户端手动发送 Google Protocol 成功={}", googleProtocolVO.toString()));
//
//    }
//
//
//    /**
//     * 1. clear route information.
//     * 2. reconnect.
//     * 3. shutdown reconnect job.
//     * 4. reset reconnect state.
//     * @throws Exception
//     */
//    public void reconnect() throws Exception {
//        if (channel != null && channel.isActive()) {
//            return;
//        }
//        //首先清除路由信息，下线
//        routeRequest.offLine();
//
//        echoService.echo("cim server shutdown, reconnecting....");
//        start();
//        echoService.echo("Great! reConnect success!!!");
//        reConnectManager.reConnectSuccess();
//        ContextHolder.clear();
//    }
//
//    /**
//     * 关闭
//     *
//     * @throws InterruptedException
//     */
//    public void close() throws InterruptedException {
//        if (channel != null){
//            channel.close();
//        }
//    }
}
