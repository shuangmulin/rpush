package com.regent.rpush.route.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.route.model.RpushPlatformConfig;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息处理平台配置表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
public interface IRpushPlatformConfigService extends IService<RpushPlatformConfig> {

    /**
     * 批量查询配置
     *
     * @param configIds 配置id列表，可以传空，传空回返回默认的配置
     * @return 键为配置id，值为：具体的配置键值
     */
    Map<Long, Map<String, String>> queryConfig(List<Long> configIds);

    /**
     * 查询配置分页数据
     *
     * @param platform 平台
     */
    ConfigTableDTO pageConfig(MessagePlatformEnum platform, Integer pageNum, Integer pageSize);
}