package com.regent.rpush.client;

/**
 * 消息处理器
 *
 * @author 钟宝林
 * @since 2021/7/10/010 20:59
 **/
public interface MsgProcessor {

    /**
     * 处理接收到的消息
     *
     * @param msg 接收到的消息
     * @return 是否需要继续执行后面的消息处理器，如果返回true，比当前的序号大消息处理将不会执行
     */
    boolean process(Msg msg);

    /**
     * 执行顺序，越小的越先执行。如果无所谓顺序，可以不用实现本方法，默认为0
     *
     * @return 执行顺序
     */
    default int getOrder() {
        return 0;
    }

}
