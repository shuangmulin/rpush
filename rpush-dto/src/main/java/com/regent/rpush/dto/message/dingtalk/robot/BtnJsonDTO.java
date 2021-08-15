package com.regent.rpush.dto.message.dingtalk.robot;

import com.regent.rpush.dto.route.sheme.MultiObjField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 按钮
 *
 * @author 钟宝林
 * @since 2021/4/11/011 14:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BtnJsonDTO {

    @MultiObjField("按钮标题")
    private String title;
    @MultiObjField("点击按钮触发的URL")
    private String actionURL;

}
