package com.regent.rpush.server.socket;

import com.regent.rpush.dto.protocol.MessageProto;
import com.regent.rpush.server.socket.client.NioSocketChannelClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 钟宝林
 * @date 2021/2/9 14:25
 **/
@ChannelHandler.Sharable
public class RpushServerHandler extends SimpleChannelInboundHandler<MessageProto.MessageProtocol> {

    /**
     * 客户端通道关闭，清除相关数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 清除相关数据
        SocketSessionHolder.offLine(NioSocketChannelClient.getInstance((NioSocketChannel) ctx.channel()));
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//            if (idleStateEvent.state() == IdleState.READER_IDLE) {
//
//                LOGGER.info("定时检测客户端端是否存活");
//
//                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class) ;
//                heartBeatHandler.process(ctx) ;
//            }
//        }
//        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto.MessageProtocol msg) throws Exception {
//        if (msg.getType() == Constants.CommandType.LOGIN) {
//            //保存客户端与 Channel 之间的关系
//            SessionSocketHolder.put(msg.getRequestId(), (NioSocketChannel) ctx.channel());
//            SessionSocketHolder.saveSession(msg.getRequestId(), msg.getReqMsg());
//            LOGGER.info("client [{}] online success!!", msg.getReqMsg());
//        }
//
//        //心跳更新时间
//        if (msg.getType() == Constants.CommandType.PING){
//            NettyAttrUtil.updateReaderTime(ctx.channel(),System.currentTimeMillis());
//            //向客户端响应 pong 消息
//            CIMRequestProto.CIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat",
//                    CIMRequestProto.CIMReqProtocol.class);
//            ctx.writeAndFlush(heartBeat).addListeners((ChannelFutureListener) future -> {
//                if (!future.isSuccess()) {
//                    LOGGER.error("IO error,close Channel");
//                    future.channel().close();
//                }
//            }) ;
//        }
    }

}
