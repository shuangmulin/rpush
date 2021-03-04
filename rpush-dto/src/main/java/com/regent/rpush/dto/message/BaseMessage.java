package com.regent.rpush.dto.message;

import com.regent.rpush.dto.BaseParam;
import com.regent.rpush.dto.message.config.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 消息基类
 *
 * @author 钟宝林
 * @date 2021/2/8 21:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage<config extends Config> extends BaseParam {
    private static final long serialVersionUID = 437493036483567460L;

    private String content;
    /**
     * 配置
     */
    private List<config> configs;

}
