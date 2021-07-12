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
 * 在线设备表，记录所有节点的在线设备
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RpushServerOnline对象", description="在线设备表，记录所有节点的在线设备")
public class RpushServerOnline extends Model {

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

    @ApiModelProperty(value = "注册设备id")
    private Long registrationId;

    @ApiModelProperty(value = "服务端id")
    private String serverId;

    @ApiModelProperty(value = "服务端host")
    private String serverHost;

    @ApiModelProperty(value = "服务端socket端口")
    private int serverSocketPort;

    @ApiModelProperty(value = "服务端端口")
    private int serverHttpPort;

    @ApiModelProperty(value = "clientId")
    private String clientId;

}
