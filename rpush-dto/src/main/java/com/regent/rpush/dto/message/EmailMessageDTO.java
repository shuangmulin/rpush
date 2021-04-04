package com.regent.rpush.dto.message;

import com.regent.rpush.dto.message.base.BaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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

    /**
     * 接收人列表
     */
    private List<String> receiverIds;
    /**
     * 接收人分组列表
     */
    private List<Long> receiverGroupIds;
    private String title;
    private String content;

}
