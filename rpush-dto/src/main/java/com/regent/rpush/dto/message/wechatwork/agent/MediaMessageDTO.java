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
 * 图片、文件消息DTO
 *
 * @author 钟宝林
 * @since 2021/4/8/008 21:38
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MediaMessageDTO extends BaseMessage {
    private static final long serialVersionUID = 7412950115675650317L;

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

    @SchemeValue(description = "素材id")
    private String mediaId;

}
