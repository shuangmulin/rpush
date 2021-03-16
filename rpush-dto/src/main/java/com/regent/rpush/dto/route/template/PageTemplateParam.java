package com.regent.rpush.dto.route.template;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 钟宝林
 * @since 2021/3/9/009 20:37
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageTemplateParam implements Serializable {
    private static final long serialVersionUID = 74660048512901203L;

    private Long id;
    @ApiModelProperty("模板名称")
    private String templateName;
    @ApiModelProperty("接收人分组ID")
    private Long receiverGroupId;

    private int pageNum;
    private int pageSize;

}
