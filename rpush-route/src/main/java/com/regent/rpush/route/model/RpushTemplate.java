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
 * 消息模板表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushTemplate对象", description="消息模板表")
public class RpushTemplate extends Model {

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

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "模板标题")
    private String title;

    @ApiModelProperty(value = "预设接收人，多个用;隔开。（对应的是rpush_template_receiver表的receiver_id）")
    private String receiverIds;

    @ApiModelProperty(value = "接收人分组id，可以为空（rpush_template_receiver_group表的id）")
    private Long receiverGroupId;

    @ApiModelProperty(value = "接收人分组名称")
    @TableField(exist = false)
    private String receiverGroupName;


}
