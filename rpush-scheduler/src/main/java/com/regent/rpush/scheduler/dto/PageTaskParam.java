package com.regent.rpush.scheduler.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 钟宝林
 * @since 2021/5/2/002 20:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageTaskParam {

    private Long id;

    private String jobName;

    private Boolean enableFlag;

    private String jobGroup;

    private int pageNum;
    private int pageSize;

}
