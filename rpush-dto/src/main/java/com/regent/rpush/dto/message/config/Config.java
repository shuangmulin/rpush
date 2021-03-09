package com.regent.rpush.dto.message.config;

import lombok.*;

import java.io.Serializable;

/**
 * 配置基类
 *
 * @author 钟宝林
 * @since 2021/3/4/004 20:24
 **/
@Data
public abstract class Config implements Serializable {
    private static final long serialVersionUID = 2765017560754006377L;

    private long configId;
    private boolean defaultFlag;
    private String configName;

}
