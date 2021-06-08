package com.regent.rpush.dto.message.dingtalk.corp;

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
 * 钉钉卡片消息-独立跳转
 *
 * @author 钟宝林
 * @since 2021/2/28/028 21:28
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

    @SchemeValue("是否发送给企业全部用户，注意钉钉限制只能发3次全员消息")
    private boolean toAllUser;

    /**
     * 接收人列表
     */
    @SchemeValue(type = SchemeValueType.RECEIVER)
    private List<String> receiverIds;

    @SchemeValue("接收人的部门id列表，接收者的部门id列表，多个用,隔开")
    private String deptIdList;

    @SchemeValue("标题，最长20个字符。")
    private String title;

    @SchemeValue("跳转链接")
    private String actionUrl;

    @SchemeValue(type = SchemeValueType.SELECT, value = "按钮排列方式", description = "使用独立跳转ActionCard样式时的按钮排列方式", options = {
            @SchemeValueOption(key = "0", label = "竖直排列"),
            @SchemeValueOption(key = "1", label = "横向排列")
    })
    private String btnOrientation = "0";

    @SchemeValue(type = SchemeValueType.TEXTAREA, description = "消息内容，支持markdown，语法参考标准markdown语法。建议1000个字符以内。")
    private String markdown;

    @SchemeValue("按钮设置")
    private List<BtnJsonDTO> btnJsonList;
}
