package com.regent.rpush.dto.message;

import com.regent.rpush.dto.message.base.BaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件消息
 *
 * @author 钟宝林
 * @date 2021/2/8 10:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMessageDTO extends BaseMessage {
    private static final long serialVersionUID = 2692273549631779696L;

    private String title;
    private String content;

}
