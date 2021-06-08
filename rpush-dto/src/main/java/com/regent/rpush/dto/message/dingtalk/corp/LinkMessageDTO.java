package com.regent.rpush.dto.message.dingtalk.corp;

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
 * 钉钉链接消息
 *
 * @author 钟宝林
 * @since 2021/4/11/011 14:40
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LinkMessageDTO extends BaseMessage {
    private static final long serialVersionUID = 6529460286674167742L;

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

    @SchemeValue("消息点击链接地址，当发送消息为小程序时支持小程序跳转链接。")
    private String messageUrl;

    @SchemeValue("图片地址，可以通过上传媒体文件接口获取。")
    private String picUrl;

    @SchemeValue("消息标题，建议100字符以内。")
    private String title;

    @SchemeValue(type = SchemeValueType.TEXTAREA, value = "消息描述，建议500字符以内。")
    private String text;

}
