package com.regent.rpush.dto.rpushserver;

import lombok.*;

/**
 * 查询在线设备参数
 *
 * @author 钟宝林
 * @since 2021/7/11/011 17:06
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageOnlineDTO {

    /**
     * 设备id
     */
    private Long registrationId;

    private Integer pageNum;
    private Integer pageSize;

}
