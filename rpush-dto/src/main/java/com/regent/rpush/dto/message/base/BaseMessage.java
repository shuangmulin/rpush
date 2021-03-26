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
    /**
     * 接收人列表
     */
    private List<String> sendTos;
    /**
     * 接收人分组列表
     */
    private List<Long> groupIds;

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setSendTos(List<String> sendTos) {
        this.sendTos = sendTos;
    }

    public List<String> getSendTos() {
        return sendTos;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }
}
