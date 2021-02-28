package com.regent.rpush.client;

import com.regent.rpush.common.protocol.MessageProto;
import com.regent.rpush.common.protocol.PingPong;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 11:50
 **/
@ChannelHandler.Sharable
public class RpushClientHandler extends SimpleChannelInboundHandler<MessageProto.MessageProtocol> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 尝试重连
        App.reconnect();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            // 向服务端发送心跳
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(PingPong.ping()).addListeners((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        future.channel().close();
                    }
                });
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto.MessageProtocol messageProtocol) throws Exception {
        System.out.println(messageProtocol);
    }
}
