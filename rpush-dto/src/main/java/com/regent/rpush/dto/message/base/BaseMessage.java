package com.regent.rpush.dto.message.base;

import com.regent.rpush.dto.BaseParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 消息基类
 *
 * @author 钟宝林
 * @date 2021/2/8 21:02
 **/
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage extends BaseParam {
    private static final long serialVersionUID = 437493036483567460L;

    /**
     * 指定clientId发送
     */
    private String clientId;
    /**
     * 配置
     */
    private List<Long> configIds;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Long> getConfigIds() {
        return configIds;
    }

    public void setConfigIds(List<Long> configIds) {
        this.configIds = configIds;
    }
}
