package com.regent.rpush.dto.route.his;

import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 钟宝林
 * @since 2021/3/18/018 22:15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageHisDetailParam {

    @ApiModelProperty("请求号")
    private String requestNo;
    @ApiModelProperty("创建日期起始")
    private Date dateCreatedStart;
    @ApiModelProperty("创建日期结束")
    private Date dateCreatedEnd;
    @ApiModelProperty("平台")
    private List<MessagePlatformEnum> platform;
    @ApiModelProperty("接收人ID")
    private String receiverId;
    @ApiModelProperty("状态")
    private List<Integer> sendStatus;

    private int pageNum;
    private int pageSize;

}
