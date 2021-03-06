package com.regent.rpush.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.mapper.RpushPlatformConfigMapper;
import com.regent.rpush.route.model.RpushPlatformConfig;
import com.regent.rpush.route.model.RpushPlatformConfigValue;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.service.IRpushPlatformConfigValueService;
import com.regent.rpush.route.utils.MessageHandlerHolder;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            valueMap.put("defaultFlag", String.valueOf(config.getDefaultFlag()));
            List<RpushPlatformConfigValue> values = configValueMap.get(config.getId());
            for (RpushPlatformConfigValue value : values) {
                valueMap.put(value.getKey(), value.getValue());
            }
        }
        return configMap;
    }

    @Override
    public ConfigTableDTO pageConfig(MessagePlatformEnum platform, Integer pageNum, Integer pageSize) {
        // 拿到对应的handler
        MessageHandler<?> messageHandler = MessageHandlerHolder.get(platform);
        if (messageHandler == null) {
            throw new IllegalStateException("还未实现的平台：" + platform.getName());
        }

        // 拿到表头
        List<ConfigFieldVO> configFieldVOS = MessageHandlerUtils.listConfigFieldName(messageHandler);
        ConfigTableDTO tableDTO = new ConfigTableDTO();
        tableDTO.setHeader(configFieldVOS);

        // 分页
        pageNum = PageUtil.getDefaultPageNum(pageNum);
        pageSize = PageUtil.getDefaultPageSize(pageSize);
        Page<RpushPlatformConfig> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushPlatformConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("platform", platform.name());
        page(page, wrapper);
        List<RpushPlatformConfig> configs = page.getRecords();
        List<Long> configIds = configs.stream().map(RpushPlatformConfig::getId).collect(Collectors.toList());

        // 查具体的配置值，并转成对应的配置实体类
        Map<Long, Map<String, String>> queryConfig = queryConfig(configIds);
        List<Config> dataList = MessageHandlerUtils.convertConfig(messageHandler, queryConfig);
        Pagination<Config> pagination = tableDTO.getPagination();
        pagination.setPageNum(pageNum);
        pagination.setDataList(dataList);
        return tableDTO;
    }

}
