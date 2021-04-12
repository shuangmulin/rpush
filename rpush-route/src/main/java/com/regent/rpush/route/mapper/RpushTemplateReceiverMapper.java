package com.regent.rpush.route.mapper;

import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 消息模板-预设接收人表 Mapper 接口
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
public interface RpushTemplateReceiverMapper extends BaseMapper<RpushTemplateReceiver> {

    @Update("${sqlStr}")
    void execute(@Param("sqlStr") String sqlStr);
}
