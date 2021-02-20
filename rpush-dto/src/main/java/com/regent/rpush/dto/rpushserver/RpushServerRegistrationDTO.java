package com.regent.rpush.dto.rpushserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 注册设备表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
@Data
@Accessors(chain = true)
@ApiModel(value="RpushServerRegistration对象", description="注册设备表")
public class RpushServerRegistrationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "记录版本")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date dateCreated;

    @ApiModelProperty(value = "更新时间")
    private Date dateUpdated;

    @ApiModelProperty(value = "名称")
    private String name;


}
