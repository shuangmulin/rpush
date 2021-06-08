package com.regent.rpush.route.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 消息处理平台配置对应的具体参数值
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushPlatformConfigValue对象", description="消息处理平台配置对应的具体参数值")
public class RpushPlatformConfigValue extends Model {

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

    @ApiModelProperty(value = "所属配置id")
    private Long configId;

    @ApiModelProperty(value = "参数键")
    @TableField("`key`")
    private String key;

    @ApiModelProperty(value = "参数值")
    @TableField("`value`")
    private String value;

    @ApiModelProperty(value = "clientId")
    private String clientId;


}
