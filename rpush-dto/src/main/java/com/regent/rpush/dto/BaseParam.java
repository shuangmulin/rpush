package com.regent.rpush.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseParam implements Serializable {

    private static final long serialVersionUID = 7375883597696884060L;

    @ApiModelProperty(value = "请求编号（幂等）")
    private String requestNo;

}
