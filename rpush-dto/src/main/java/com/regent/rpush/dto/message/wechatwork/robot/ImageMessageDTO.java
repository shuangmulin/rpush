package com.regent.rpush.dto.message.wechatwork.robot;

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
public class ImageMessageDTO extends BaseMessage {
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

    @SchemeValue(description = "图片内容的base64编码")
    private String base64;

    @SchemeValue(description = "图片内容（base64编码前）的md5值")
    private String md5;

}
