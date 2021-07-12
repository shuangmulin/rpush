package com.regent.rpush.client;

import com.regent.rpush.common.protocol.MessageProto;
import com.regent.rpush.common.protocol.PingPong;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.List;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 11:50
 **/
@ChannelHandler.Sharable
public class RpushClientHandler extends SimpleChannelInboundHandler<MessageProto.MessageProtocol> {

    private final RpushClient rpushClient;

    public RpushClientHandler(RpushClient rpushClient) {
        this.rpushClient = rpushClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 尝试重连
        rpushClient.reconnect();
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
        Msg msg = Msg.builder()
                .content(messageProtocol.getContent())
                .fromTo(messageProtocol.getFromTo())
                .sendTo(messageProtocol.getSendTo())
                .type(messageProtocol.getType())
                .build();

        List<MsgProcessor> msgProcessors = rpushClient.getMsgProcessors();
        for (MsgProcessor msgProcessor : msgProcessors) {
            boolean process = msgProcessor.process(msg);
            if (process) {
                // 返回false，不再继续执行
                break;
            }
        }
    }
}
