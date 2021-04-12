package com.regent.rpush.route.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 批量插入DTO
 *
 * @author 钟宝林
 * @since 2021/4/12/012 10:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiverBatchInsertDTO implements Serializable {
    private static final long serialVersionUID = 7346154462311221544L;

    /**
     * 接收人ID
     */
    @ExcelProperty("接收人ID")
    private String receiverId;
    /**
     * 接收人名称
     */
    @ExcelProperty("姓名")
    private String receiverName;
    /**
     * 接收人分组名称
     */
    @ExcelProperty("分组名称")
    private String receiverGroupName;

}
