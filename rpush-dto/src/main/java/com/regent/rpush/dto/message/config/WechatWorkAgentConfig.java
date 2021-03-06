package com.regent.rpush.dto.message.config;

import com.regent.rpush.dto.route.config.ConfigValue;
import lombok.*;

/**
 * 企业微信配置
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatWorkAgentConfig extends Config {
    private static final long serialVersionUID = -9206902816158196669L;

    @ConfigValue(value = "企业ID", description = "在此页面查看：https://work.weixin.qq.com/wework_admin/frame#profile")
    private String corpId;
    @ConfigValue(value = "应用Secret")
    private String secret;
    @ConfigValue(value = "应用agentId")
    private Integer agentId;

}
