package com.regent.rpush.dto.message.wechatofficialaccount;

import com.regent.rpush.dto.route.sheme.MultiObjField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信模板Data
 *
 * @author 钟宝林
 * @since 2021/4/10/010 15:35
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatTemplateData {

    private String name;
    private String value;
    @MultiObjField(value = "显示颜色")
    private String color;

}
