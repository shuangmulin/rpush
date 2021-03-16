package com.regent.rpush.dto.message;

import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.message.config.WechatWorkConfig;
import lombok.*;

/**
 * 企业微信消息发送DTO
 *
 * @author 钟宝林
 * @since 2021/2/28/028 21:28
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class WechatWorkMessageDTO extends BaseMessage<WechatWorkConfig> {
    private static final long serialVersionUID = -3289428483627765265L;

}
