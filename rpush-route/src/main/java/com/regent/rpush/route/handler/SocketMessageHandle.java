package com.regent.rpush.route.handler;

import com.regent.rpush.api.server.MessagePushService;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.NormalMessageDTO;
import com.regent.rpush.dto.message.SocketMessageDTO;
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
public class SocketMessageHandle extends MessageHandler<SocketMessageDTO> {

    @Autowired
    private MessagePushService messagePushService;

    @Override
    public MessagePlatformEnum platform() {
        return MessagePlatformEnum.RPUSH_SERVER;
    }

    @Override
    public void handle(SocketMessageDTO param) {
        List<Long> sendTos = param.getSendTos();
        Long fromTo = param.getFromTo();

        for (Long sendTo : sendTos) {
            NormalMessageDTO build = NormalMessageDTO.builder()
                    .fromTo(fromTo)
                    .sendTo(sendTo)
                    .build();
            build.setContent(param.getContent());
            messagePushService.push(build);
        }
    }
}
