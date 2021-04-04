package com.regent.rpush.dto.message.base;

import com.regent.rpush.dto.BaseParam;
import com.regent.rpush.dto.message.config.Config;

import java.util.List;

/**
 * 消息基类
 *
 * @author 钟宝林
 * @date 2021/2/8 21:02
 **/
public class BaseMessage extends BaseParam {
    private static final long serialVersionUID = 437493036483567460L;

    /**
     * 配置
     */
    private List<Config> configs;

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public List<Config> getConfigs() {
        return configs;
    }
}
