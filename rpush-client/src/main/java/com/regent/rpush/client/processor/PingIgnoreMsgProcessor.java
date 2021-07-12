package com.regent.rpush.client.processor;

import com.regent.rpush.client.Msg;
import com.regent.rpush.client.MsgProcessor;
import com.regent.rpush.common.Constants;

/**
 * 忽略心跳
 *
 * @author 钟宝林
 * @since 2021/7/10/010 21:32
 **/
public class PingIgnoreMsgProcessor implements MsgProcessor {
    @Override
    public boolean process(Msg msg) {
        return msg.getType() == Constants.MessageType.PING;
    }

    @Override
    public int getOrder() {
        return -9999;
    }
}
