package com.regent.rpush.dto.message.dingtalk.robot;

import com.regent.rpush.dto.enumration.SchemeValueType;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.route.sheme.MultiObjField;
import com.regent.rpush.dto.route.sheme.SchemeValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 钉钉群消息FeedCard类型DTO
 *
 * @author 钟宝林
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCardMessageDTO extends BaseMessage {
    private static final long serialVersionUID = -3289428483627765265L;

    /**
     * 接收人分组列表
     */
    @SchemeValue(type = SchemeValueType.RECEIVER_GROUP)
    private List<Long> receiverGroupIds;

    @SchemeValue("是否@所有人")
    private boolean isAtAll;

    /**
     * 接收人列表
     */
    @SchemeValue(type = SchemeValueType.RECEIVER)
    private List<String> receiverIds;

    @SchemeValue("多条信息设置")
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        @MultiObjField("单条信息文本")
        private String title;
        @MultiObjField("点击单条信息的链接")
        private String messageURL;
        @MultiObjField("单条信息图片的URL")
        private String picURL;
    }

}
