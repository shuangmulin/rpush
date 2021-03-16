package com.regent.rpush.dto.route;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 钟宝林
 * @since 2021/3/5/005 17:54
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformDTO implements Serializable {
    private static final long serialVersionUID = 5135216840223100433L;

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty(value = "平台中文名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "格式校验用的正则表达式")
    private String validateReg;
    @ApiModelProperty(value = "是否启用")
    private boolean enable;

}
