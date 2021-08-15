package com.regent.rpush.dto.message.dingtalk.robot;

import com.regent.rpush.dto.enumration.SchemeValueType;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.route.sheme.SchemeValue;
import com.regent.rpush.dto.route.sheme.SchemeValueOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 钉钉群消息-卡片消息-独立跳转类型DTO
 *
 * @author 钟宝林
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ActionCardMultiMessageDTO extends BaseMessage {
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

    @SchemeValue(description = "首屏会话透出的展示内容")
    private String title;

    @SchemeValue(type = SchemeValueType.TEXTAREA, description = "markdown格式的消息")
    private String text;

    @SchemeValue(type = SchemeValueType.SELECT, value = "按钮排列方式", description = "使用独立跳转ActionCard样式时的按钮排列方式", options = {
            @SchemeValueOption(key = "0", label = "按钮竖直排列"),
            @SchemeValueOption(key = "1", label = "按钮横向排列")
    })
    private String btnOrientation = "0";

    @SchemeValue("按钮")
    private List<BtnJsonDTO> btns;

}
