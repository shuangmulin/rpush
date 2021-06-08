package com.regent.rpush.dto.message;

import com.regent.rpush.dto.enumration.SchemeValueType;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.route.sheme.SchemeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 邮件消息
 *
 * @author 钟宝林
 * @date 2021/2/8 10:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO extends BaseMessage {
    private static final long serialVersionUID = 2692273549631779696L;

    /**
     * 接收人分组列表
     */
    @SchemeValue(type = SchemeValueType.RECEIVER_GROUP)
    private List<Long> receiverGroupIds;
    /**
     * 接收人列表
     */
    @SchemeValue(type = SchemeValueType.RECEIVER)
    private List<String> receiverIds;
    @SchemeValue(description = "请输入邮箱标题...")
    private String title;
    @SchemeValue(type = SchemeValueType.TEXTAREA, description = "请输入邮箱内容...")
    private String content;

}
