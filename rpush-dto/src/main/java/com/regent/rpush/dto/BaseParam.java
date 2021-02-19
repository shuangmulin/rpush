package com.regent.rpush.dto;

import lombok.*;

import java.io.Serializable;

@Data
public class BaseParam implements Serializable {

    private static final long serialVersionUID = 7375883597696884060L;
    /**
     * 请求编号（幂等）
     */
    private String requestNo;

}
