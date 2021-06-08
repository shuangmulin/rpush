package com.regent.rpush.dto.rpushserver;

import com.regent.rpush.dto.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 下线DTO
 *
 * @author 钟宝林
 * @since 2021/2/20/020 16:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OfflineDTO extends BaseParam {
    private static final long serialVersionUID = -2239907502712723992L;

    @ApiModelProperty("设备ID")
    private long registrationId;

}
