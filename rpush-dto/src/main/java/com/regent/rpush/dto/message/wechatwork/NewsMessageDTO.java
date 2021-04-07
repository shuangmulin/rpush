package com.regent.rpush.dto.message.wechatwork;

import com.regent.rpush.dto.message.base.BaseMessage;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业微信图文消息发送DTO
 *
 * @author 钟宝林
 * @since 2021/4/7/007 17:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class NewsMessageDTO extends BaseMessage {
    private static final long serialVersionUID = 7034106110120563906L;



}
