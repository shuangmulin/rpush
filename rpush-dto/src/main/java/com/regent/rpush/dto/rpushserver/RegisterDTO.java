package com.regent.rpush.dto.rpushserver;

import com.regent.rpush.dto.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 注册DTO
 *
 * @author 钟宝林
 * @since 2021/2/20/020 16:16
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO extends BaseParam {
    private static final long serialVersionUID = -5509660044239497024L;

    @ApiModelProperty(value = "名称")
    private String name;

}
