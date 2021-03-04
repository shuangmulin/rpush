package com.regent.rpush.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.route.mapper.RpushPlatformConfigMapper;
import com.regent.rpush.route.model.RpushPlatformConfig;
import com.regent.rpush.route.model.RpushPlatformConfigValue;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.service.IRpushPlatformConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 消息处理平台配置表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
@Service
public class RpushPlatformConfigServiceImpl extends ServiceImpl<RpushPlatformConfigMapper, RpushPlatformConfig> implements IRpushPlatformConfigService {

    @Autowired
    private IRpushPlatformConfigValueService rpushPlatformConfigValueService;

    @Override
    public Map<Long, Map<String, String>> queryConfig(List<Long> configIds) {
        if (configIds == null || configIds.size() <= 0) {
            QueryWrapper<RpushPlatformConfig> queryWrapper = new QueryWrapper<>();
            RpushPlatformConfig config = getOne(queryWrapper);
            if (config == null) {
                return new HashMap<>();
            }
            configIds = Collections.singletonList(config.getId());
        }

        Collection<RpushPlatformConfig> configs = listByIds(configIds);
        QueryWrapper<RpushPlatformConfigValue> configValueQueryWrapper = new QueryWrapper<>();
        configValueQueryWrapper.in("config_id", configIds);
        List<RpushPlatformConfigValue> configValues = rpushPlatformConfigValueService.list(configValueQueryWrapper);
        Map<Long, List<RpushPlatformConfigValue>> configValueMap = new HashMap<>();
        for (RpushPlatformConfigValue configValue : configValues) {
            configValueMap.computeIfAbsent(configValue.getConfigId(), k -> new ArrayList<>()).add(configValue);
        }

        Map<Long, Map<String, String>> configMap = new HashMap<>(); // 键为配置id，值为：具体的配置键值
        for (RpushPlatformConfig config : configs) {
            Map<String, String> valueMap = configMap.computeIfAbsent(config.getId(), k -> new HashMap<>());
            List<RpushPlatformConfigValue> values = configValueMap.get(config.getId());
            for (RpushPlatformConfigValue value : values) {
                valueMap.put(value.getKey(), value.getValue());
            }
        }
        return configMap;
    }
}
