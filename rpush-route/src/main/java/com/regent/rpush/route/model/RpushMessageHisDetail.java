package com.regent.rpush.route.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 消息历史记录详情表
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushMessageHisDetail对象", description="消息历史记录详情表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpushMessageHisDetail extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 状态-未开始
     */
    public static final int SEND_STATUS_NOT_START = 0;
    /**
     * 状态-成功
     */
    public static final int SEND_STATUS_SUCCESS = 1;
    /**
     * 状态-失败
     */
    public static final int SEND_STATUS_FAIL = 2;

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

    @ApiModelProperty(value = "接收人id")
    private String receiverId;

    @ApiModelProperty(value = "配置id")
    private Long configId;

    @ApiModelProperty(value = "配置名称（冗余配置，这个字段具有一定的时效性，具体配置可能会改）")
    private String configName;

    @ApiModelProperty(value = "所属请求号，对应表rpush_message_his的字段request_no")
    private String requestNo;

    @ApiModelProperty(value = "发送状态，0 未开始；1 发送成功；2 发送失败")
    private Integer sendStatus;

    @ApiModelProperty(value = "错误消息")
    private String errorMsg;


}
