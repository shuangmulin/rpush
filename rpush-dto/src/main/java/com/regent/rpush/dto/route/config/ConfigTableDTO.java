package com.regent.rpush.dto.route.config;

import com.regent.rpush.dto.table.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 表格DTO
 *
 * @author 钟宝林
 * @since 2021/3/6/006 15:35
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigTableDTO implements Serializable {
    private static final long serialVersionUID = 1503575543714968318L;

    @ApiModelProperty("表头")
    private List<ConfigFieldVO> header;

    @ApiModelProperty("分页数据")
    private Pagination<Map<String, Object>> pagination = new Pagination<>(1, 20);

}
