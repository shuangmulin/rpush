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
 * 消息模板-预设接收人表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushTemplateReceiver对象", description="消息模板-预设接收人表")
public class RpushTemplateReceiver extends Model {

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

    @ApiModelProperty(value = "所属分组id")
    private Long groupId;

    @ApiModelProperty("分组名称")
    @TableField(exist = false)
    private String groupName;

    @ApiModelProperty(value = "接收人，用来发消息的id性质的字段，如果是邮箱就是要发送的邮箱，如果是企业微信就是企业微信对应用户的id，如果是rpush就是registrationId")
    private String receiverId;

    @ApiModelProperty(value = "接收人名称，显示用的字段，可以为空")
    private String receiverName;

    @ApiModelProperty(value = "头像，显示用的字段，可以为空")
    private String profilePhoto;

    @ApiModelProperty(value = "clientId")
    private String clientId;


}
