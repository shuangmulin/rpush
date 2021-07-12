package com.regent.rpush.route.handler;

import com.regent.rpush.api.server.MessagePushService;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.NormalMessageDTO;
import com.regent.rpush.dto.message.RpushMessageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Socket消息处理
 *
 * @author 钟宝林
 * @since 2021/2/28/028 21:27
 **/
@Component
public class RpushMessageHandler extends MessageHandler<RpushMessageDTO> {

    @Autowired
    private MessagePushService messagePushService;

    @Override
    public MessageType messageType() {
        return MessageType.RPUSH_SERVER;
    }

    @Override
    public void handle(RpushMessageDTO param) {
        List<String> sendTos = param.getReceiverIds();

        for (String sendTo : sendTos) {
            if (!StringUtils.isNumeric(sendTo)) {
                continue;
            }
            NormalMessageDTO build = NormalMessageDTO.builder()
                    .fromTo(param.getFromTo())
                    .sendTo(Long.parseLong(sendTo))
                    .build();
            build.setContent(param.getContent());
            messagePushService.push(build);
        }
    }
}
