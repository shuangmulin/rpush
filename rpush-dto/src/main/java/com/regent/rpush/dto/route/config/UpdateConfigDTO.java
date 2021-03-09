package com.regent.rpush.dto.route.config;

import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 更新配置DTO
 *
 * @author 钟宝林
 * @since 2021/3/9/009 16:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateConfigDTO implements Serializable {
    private static final long serialVersionUID = -1623744940584029569L;

    @NotNull(message = "未知平台")
    private MessagePlatformEnum platform;
    private Long configId;
    private String configName;
    @ApiModelProperty("键对应配置的key，值对应配置的value")
    private Map<String, String> config;

}
