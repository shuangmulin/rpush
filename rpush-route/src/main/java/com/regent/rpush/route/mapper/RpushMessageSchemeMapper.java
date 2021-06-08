package com.regent.rpush.route.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rpush.dto.common.IdAndName;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.route.model.RpushMessageScheme;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消息发送方案 Mapper 接口
 * </p>
 *
 * @author 钟宝林
 * @since 2021-04-05
 */
public interface RpushMessageSchemeMapper extends BaseMapper<RpushMessageScheme> {

    @Select("SELECT " +
            " rms.id AS id, " +
            " rms.`name` AS `name` " +
            "FROM " +
            " rpush_message_scheme AS rms " +
            "WHERE " +
            " rms.client_id = #{clientId}" +
            "AND rms.message_type = #{messageType}")
    List<IdAndName> listScheme(@Param("clientId") String clientId, @Param("messageType") MessageType messageType);
}
