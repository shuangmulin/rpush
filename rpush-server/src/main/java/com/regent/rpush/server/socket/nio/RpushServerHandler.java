package com.regent.rpush.server.socket.nio;

import com.regent.rpush.common.Constants;
import com.regent.rpush.common.protocol.MessageProto;
import com.regent.rpush.common.protocol.PingPong;
import com.regent.rpush.server.socket.RpushClient;
import com.regent.rpush.server.socket.session.SocketSession;
import com.regent.rpush.server.socket.session.SocketSessionHolder;
import com.regent.rpush.server.utils.SpringConfig;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 钟宝林
 * @date 2021/2/9 14:25
 **/
@ChannelHandler.Sharable
public class RpushServerHandler extends SimpleChannelInboundHandler<MessageProto.MessageProtocol> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpushServerHandler.class);

    /**
     * 客户端通道关闭，清除相关数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 清除相关数据
        SocketSessionHolder.offLine(NioSocketChannelClient.getInstance(ctx.channel()));
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                LOGGER.info("定时检测客户端是否存活");

                long heartBeatTime = SpringConfig.getHeartbeatTime() * 1000;
                SocketSession socketSession = SocketSessionHolder.get(NioSocketChannelClient.getInstance(ctx.channel()));
                Long lastPingTime = (Long) socketSession.getAttribute(SocketSessionKey.LAST_PING_TIME);
                long now = System.currentTimeMillis();
                if (lastPingTime != null && now - lastPingTime > heartBeatTime) {
                    Long registrationId = socketSession.getRegistrationId();
                    if (registrationId != null) {
                        LOGGER.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!", registrationId, now - lastPingTime);
                    }
                    SocketSessionHolder.offLine((RpushClient) ctx.channel());
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProto.MessageProtocol msg) throws Exception {
        if (msg.getType() == Constants.MessageType.LOGIN) {
            // 收到客户端登录请求
            SocketSessionHolder.login(msg.getFromTo(), NioSocketChannelClient.getInstance(ctx.channel()));
            LOGGER.info("client [{}] online success!!", msg.getFromTo());
        }

        // 收到客户端心跳，更新对应客户端最新心跳时间
        if (msg.getType() == Constants.MessageType.PING) {
            SocketSession socketSession = SocketSessionHolder.get(NioSocketChannelClient.getInstance(ctx.channel()));
            socketSession.setAttribute(SocketSessionKey.LAST_PING_TIME, System.currentTimeMillis());
            // 向客户端响应 pong 消息
            ctx.writeAndFlush(PingPong.pong()).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("IO error,close Channel");
                    future.channel().close();
                }
            });
        }
    }

}
