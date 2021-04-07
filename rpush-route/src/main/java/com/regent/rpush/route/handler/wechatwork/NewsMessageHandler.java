package com.regent.rpush.route.handler.wechatwork;

import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.wechatwork.NewsMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;

/**
 * 企业微信图文消息handler
 *
 * @author 钟宝林
 * @since 2021/4/7/007 17:28
 **/
public class NewsMessageHandler extends MessageHandler<NewsMessageDTO> {
    @Override
    public MessageType messageType() {
        return null;
    }

    @Override
    public void handle(NewsMessageDTO param) {

    }
}
