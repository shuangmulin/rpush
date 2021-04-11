package com.regent.rpush.dto.message.config;

import com.regent.rpush.dto.route.config.ConfigValue;
import lombok.*;

/**
 * 钉钉工作通知配置
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DingTalkCorpConfig extends Config {
    private static final long serialVersionUID = -9206902816158196669L;

    @ConfigValue(value = "应用appKey")
    private String appKey;
    @ConfigValue(value = "应用Secret")
    private String AppSecret;
    @ConfigValue(value = "应用agentId")
    private Integer agentId;

}
