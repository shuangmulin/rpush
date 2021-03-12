package com.regent.rpush.dto.route.template.receiver;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author 钟宝林
 * @since 2021/3/10/010 20:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageReceiverParam implements Serializable {
    private static final long serialVersionUID = -5607981746602299761L;

    private Long id;
    @ApiModelProperty("分组id")
    private Long groupId;
    @ApiModelProperty("接收人")
    private String receiverId;
    @ApiModelProperty("接收人名称")
    private String receiverName;

    private int pageNum;
    private int pageSize;
}
