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
public class BaseMessage<config extends Config> extends BaseParam {
    private static final long serialVersionUID = 437493036483567460L;

    private String content;
    /**
     * 配置
     */
    private List<config> configs;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SuppressWarnings("unchecked")
    public void setConfigs(List<Config> configs) {
        this.configs = (List<config>) configs;
    }

    public List<config> getConfigs() {
        return configs;
    }
}
