package com.regent.rpush.route.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息处理平台配置表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushPlatformConfig对象", description="消息处理平台配置表")
public class RpushPlatformConfig extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "记录版本")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date dateCreated;

    @ApiModelProperty(value = "更新时间")
    private Date dateUpdated;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "配置的名称（显示用的字段）")
    private String configName;

    @ApiModelProperty(value = "默认标识，0标识不是默认配置，1标识是默认配置")
    private Boolean defaultFlag;


}
