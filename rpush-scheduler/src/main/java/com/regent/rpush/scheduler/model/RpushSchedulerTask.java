package com.regent.rpush.scheduler.model;

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
 * 调度任务表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushSchedulerTask对象", description="调度任务表")
public class RpushSchedulerTask extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名")
    private String jobName;

    @ApiModelProperty(value = "任务描述")
    private String description;

    @NotBlank(message = "cron表达式不能为空")
    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "任务执行时调用哪个类的方法 包名+类名")
    private String beanClass;

    @ApiModelProperty(value = "启用标志，true表示已启用，false表示未启用")
    private Boolean enableFlag;

    @NotBlank(message = "任务分组不能为空")
    @ApiModelProperty(value = "任务分组")
    private String jobGroup;

    @NotBlank(message = "任务参数不能为空")
    @ApiModelProperty(value = "附带的参数，json格式")
    private String param;

    @ApiModelProperty(value = "创建时间")
    private Date dateCreated;

    @ApiModelProperty(value = "更新时间")
    private Date dateUpdated;

    @NotNull(message = "预设开始时间不能为空")
    @ApiModelProperty(value = "预设开始时间")
    private Date startAt;

    @NotNull(message = "预设结束时间不能为空")
    @ApiModelProperty(value = "预设结束时间")
    private Date endAt;

    @ApiModelProperty(value = "clientId")
    private String clientId;


}
