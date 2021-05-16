package com.regent.rpush.route.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 消息模板-接收人分组表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushTemplateReceiverGroup对象", description="消息模板-接收人分组表")
public class RpushTemplateReceiverGroup extends Model {

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

    @NotNull(message = "未知平台")
    @ApiModelProperty(value = "所属平台")
    private String platform;

    @NotBlank(message = "分组名称必填")
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "clientId")
    private String clientId;


}
