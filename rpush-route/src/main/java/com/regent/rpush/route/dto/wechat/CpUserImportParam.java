package com.regent.rpush.route.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 企业微信导入参数
 *
 * @author 钟宝林
 * @since 2021/4/19/019 23:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpUserImportParam {

    private String corpId;
    private String secret;
    private Integer agentId;

    @ApiModelProperty("导入方式，0 导入所有用户 1 按部门导入")
    private int importStyle;

    @ApiModelProperty("选择分组id")
    private Long groupId;

    @ApiModelProperty("按部门导入方式，0 按部门名称创建 1 导入指定分组")
    private int departmentImportStyle;

    @ApiModelProperty("选择的部门列表")
    private List<Long> departmentIds;

}
