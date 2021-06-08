package com.regent.rpush.route.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.dto.route.config.UpdateConfigDTO;
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
     * @param configIds 配置id列表，传空会返回空map
     * @return 键为配置id，值为：具体的配置键值
     */
    Map<Long, Map<String, Object>> queryConfig(String clientId, List<Long> configIds);

    /**
     * 批量查询配置，如果没有传配置id列表，会查一下默认配置，如果没有默认配置，则会返回空数组
     *
     * @param clientId   clientId
     * @param configIds  配置id列表
     * @param configType 配置DTO类型
     */
    <T> List<T> queryConfigOrDefault(String clientId, List<Long> configIds, Class<T> configType, MessagePlatformEnum platform);
    <T> List<T> queryConfigOrDefault(BaseMessage message, Class<T> configType, MessagePlatformEnum platform);

    /**
     * 查询配置分页数据
     *
     * @param platform 平台
     */
    ConfigTableDTO pageConfig(MessagePlatformEnum platform, Long configId, String configName, Integer pageNum, Integer pageSize);

    /**
     * 更新配置
     */
    void updateConfig(UpdateConfigDTO updateConfigDTO);

    /**
     * 设为默认
     *
     * @param configId    配置id
     * @param defaultFlag true 或者 false
     */
    void setDefault(String configId, boolean defaultFlag);

    /**
     * 删除某个配置
     *
     * @param configId 配置id
     */
    void delete(Long configId);
}
