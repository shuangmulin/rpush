package com.regent.rpush.dto.message.wechatwork.agent;

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
 * 视频消息类型DTO
 *
 * @author 钟宝林
 * @since 2021/4/8/008 21:49
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TextCardMessageDTO extends BaseMessage {
    private static final long serialVersionUID = -5830938694539681793L;

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

    @SchemeValue(description = "PartyID列表，非必填，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
    private String toParty;

    @SchemeValue(description = "TagID列表，非必填，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
    private String toTag;

    @SchemeValue(description = "点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https)")
    private String url;

    @SchemeValue(description = "标题，不超过128个字节，超过会自动截断")
    private String title;

    @SchemeValue(description = "描述，不超过512个字节，超过会自动截断")
    private String description;

    @SchemeValue(description = "按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。")
    private String btntxt;
}
