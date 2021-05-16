package com.regent.rpush.route.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 消息发送方案
 * </p>
 *
 * @author 钟宝林
 * @since 2021-04-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushMessageScheme对象", description="消息发送方案")
public class RpushMessageScheme extends Model {

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

    @ApiModelProperty(value = "参数，json字符串")
    private String param;

    @ApiModelProperty(value = "所属平台")
    private String platform;

    @ApiModelProperty(value = "所属消息类型")
    private String messageType;

    @ApiModelProperty(value = "方案名称")
    private String name;

    @ApiModelProperty(value = "clientId")
    private String clientId;

}
