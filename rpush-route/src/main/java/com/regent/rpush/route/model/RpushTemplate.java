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
 * 消息模板表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
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

    @ApiModelProperty(value = "模板内容")
    private String content;


}
