package com.regent.rpush.route.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.enumration.ConfigValueType;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.dto.route.config.UpdateConfigDTO;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.mapper.RpushPlatformConfigMapper;
import com.regent.rpush.route.model.RpushPlatformConfig;
import com.regent.rpush.route.model.RpushPlatformConfigValue;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.service.IRpushPlatformConfigValueService;
import com.regent.rpush.route.service.IRpushTemplateService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import com.regent.rpush.route.utils.Qw;
import com.regent.rpush.route.config.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private IRpushTemplateService rpushTemplateService;

    @Override
    public Map<Long, Map<String, Object>> queryConfig(String clientId, List<Long> configIds) {
        if (configIds == null || configIds.size() <= 0) {
            return new HashMap<>();
        }
        Collection<RpushPlatformConfig> configs = listByIds(configIds);
        QueryWrapper<RpushPlatformConfigValue> configValueQueryWrapper = new QueryWrapper<>();
        configValueQueryWrapper.in("client_id", clientId);
        configValueQueryWrapper.in("config_id", configIds);
        List<RpushPlatformConfigValue> configValues = rpushPlatformConfigValueService.list(configValueQueryWrapper);
        Map<Long, List<RpushPlatformConfigValue>> configValueMap = new HashMap<>();
        for (RpushPlatformConfigValue configValue : configValues) {
            configValueMap.computeIfAbsent(configValue.getConfigId(), k -> new ArrayList<>()).add(configValue);
        }

        Map<Long, Map<String, Object>> configMap = new HashMap<>(); // 键为配置id，值为：具体的配置键值
        for (RpushPlatformConfig config : configs) {
            Map<String, Object> valueMap = configMap.computeIfAbsent(config.getId(), k -> new HashMap<>());
            valueMap.put("defaultFlag", config.getDefaultFlag());
            valueMap.put("configName", config.getConfigName());
            List<RpushPlatformConfigValue> values = configValueMap.get(config.getId());
            if (values == null || values.size() <= 0) {
                continue;
            }
            for (RpushPlatformConfigValue value : values) {
                valueMap.put(value.getKey(), value.getValue());
            }
        }
        return configMap;
    }

    @Override
    public <T> List<T> queryConfigOrDefault(String clientId, List<Long> configIds, Class<T> configType, MessagePlatformEnum platform) {
        if (configIds == null || configIds.size() <= 0) {
            // 查一个默认配置出来用
            QueryWrapper<RpushPlatformConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("client_id", clientId);
            queryWrapper.eq("platform", platform.name());
            queryWrapper.eq("default_flag", true);
            RpushPlatformConfig config = getOne(queryWrapper, false);
            if (config == null) {
                return new ArrayList<>();
            }
            configIds = Collections.singletonList(config.getId());
        }
        Map<Long, Map<String, Object>> configMap = queryConfig(clientId, configIds); // 键为配置id，值为：具体的配置键值
        List<Config> configs = MessageHandlerUtils.convertConfig(configType, configMap); // 转成具体的配置实体类
        //noinspection unchecked
        return (List<T>) configs;
    }

    @Override
    public <T> List<T> queryConfigOrDefault(BaseMessage message, Class<T> configType, MessagePlatformEnum platform) {
        return queryConfigOrDefault(message.getClientId(), message.getConfigIds(), configType, platform);
    }

    @Override
    public ConfigTableDTO pageConfig(MessagePlatformEnum platform, Long configId, String configName, Integer pageNum, Integer pageSize) {
        // 拿到表头
        List<ConfigFieldVO> configFieldVOS = MessageHandlerUtils.listConfigFieldName(platform);
        ConfigTableDTO tableDTO = new ConfigTableDTO();
        tableDTO.setHeader(configFieldVOS);

        // 分页
        pageNum = PageUtil.getDefaultPageNum(pageNum);
        pageSize = PageUtil.getDefaultPageSize(pageSize);
        Page<RpushPlatformConfig> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushPlatformConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("client_id", SessionUtils.getClientId());
        wrapper.eq("platform", platform.name());
        wrapper.like(StringUtils.isNotBlank(configName), "config_name", configName);
        wrapper.eq(configId != null, "id", configId);
        page = (Page<RpushPlatformConfig>) page(page, wrapper);
        List<RpushPlatformConfig> configs = page.getRecords();
        List<Long> configIds = configs.stream().map(RpushPlatformConfig::getId).collect(Collectors.toList());

        // 查具体的配置值
        Map<Long, Map<String, Object>> queryConfig = queryConfig(SessionUtils.getClientId(), configIds);
        Collection<Map<String, Object>> dataList = queryConfig.values();
        for (Map.Entry<Long, Map<String, Object>> entry : queryConfig.entrySet()) {
            entry.getValue().put("configId", entry.getKey());
        }
        Pagination<Map<String, Object>> pagination = tableDTO.getPagination();
        pagination.setPageNum(pageNum);
        pagination.setDataList(new ArrayList<>(dataList));
        pagination.setTotal((int) page.getTotal());

        // 补一下模板名称
        fillTemplateName(tableDTO, configFieldVOS, dataList);

        return tableDTO;
    }

    private void fillTemplateName(ConfigTableDTO tableDTO, List<ConfigFieldVO> configFieldVOS, Collection<Map<String, Object>> dataList) {
        if (configFieldVOS == null || dataList == null || dataList.size() <= 0) {
            return;
        }
        List<ConfigFieldVO> fieldVOS = new ArrayList<>(configFieldVOS);
        String rpushTemplateKey = "";
        int size = fieldVOS.size();
        for (int i = 0; i < size; i++) {
            ConfigFieldVO fieldVO = fieldVOS.get(i);
            ConfigValueType type = fieldVO.getType();
            rpushTemplateKey = fieldVO.getKey();
            if (ConfigValueType.RPUSH_TEMPLATE.equals(type)) {
                fieldVOS.add(i, ConfigFieldVO.builder().key(rpushTemplateKey + "Name").name(fieldVO.getName() + "名称").build());
                i++;
                size++;
            }
        }
        if (StringUtils.isNotBlank(rpushTemplateKey)) {
            String finalRpushTemplateKey = rpushTemplateKey;
            List<Long> templateIds = dataList.stream().map(config -> {
                String templateIdStr = (String) config.get(finalRpushTemplateKey);
                if (!NumberUtil.isNumber(templateIdStr)) {
                    return null;
                }
                return Long.parseLong(templateIdStr);
            }).collect(Collectors.toList());
            templateIds.removeIf(Objects::isNull);
            if (templateIds.size() > 0) {
                Collection<RpushTemplate> rpushTemplates = rpushTemplateService.listByIds(templateIds);
                Map<Long, String> templateNameMap = rpushTemplates.stream().collect(Collectors.toMap(RpushTemplate::getId, RpushTemplate::getTemplateName));
                for (Map<String, Object> config : dataList) {
                    String templateIdStr = String.valueOf(config.get(rpushTemplateKey));
                    if (!NumberUtil.isNumber(templateIdStr)) {
                        continue;
                    }
                    long templateId = Long.parseLong(templateIdStr);
                    config.put(rpushTemplateKey + "Name", templateNameMap.get(templateId));
                }
            }
        }
        tableDTO.setHeader(fieldVOS);
    }

    @Transactional
    @Override
    public void updateConfig(UpdateConfigDTO updateConfigDTO) {
        Map<String, String> config = updateConfigDTO.getConfig();
        if (config != null) {
            config.remove("configId");
            config.remove("configName");
            config.remove("defaultFlag");
        }
        Long configId = updateConfigDTO.getConfigId();
        configId = configId != null && configId <= 0L ? null : configId;
        MessagePlatformEnum platform = updateConfigDTO.getPlatform();
        String configName = updateConfigDTO.getConfigName();
        String clientId = SessionUtils.getClientId();

        boolean isUpdate = configId != null;
        boolean isAdd = configId == null; // 有传配置id认为是更新，没有传id认为是新增

        if (isAdd) {
            Assert.isTrue(config != null && config.size() > 0, "配置参数不全");
            Assert.isTrue(StringUtils.isNotBlank(configName), "新增配置名称不能为空");
        }

        if (StringUtils.isNotBlank(configName)) {
            // 名称判重
            QueryWrapper<RpushPlatformConfig> configNameQw = Qw.newInstance(RpushPlatformConfig.class)
                    .eq("client_id", clientId)
                    .eq("platform", platform)
                    .eq("config_name", configName);
            if (configId != null) {
                configNameQw.ne("id", configId);
            }
            RpushPlatformConfig existsConfig = getOne(configNameQw);
            Assert.isTrue(existsConfig == null, "配置名称重复");
        }

        RpushPlatformConfig rpushPlatformConfig;
        // 主表入库
        if (isUpdate) {
            rpushPlatformConfig = getById(configId);
            rpushPlatformConfig.setConfigName(configName);
            updateById(rpushPlatformConfig);
        } else {
            rpushPlatformConfig = new RpushPlatformConfig();
            rpushPlatformConfig.setClientId(clientId);
            rpushPlatformConfig.setConfigName(configName);
            rpushPlatformConfig.setPlatform(platform.name());
            save(rpushPlatformConfig);
        }

        if (config == null) {
            // 只更新主表
            return;
        }

        if ((config.size() <= 0)) {
            // 只更新主表
            return;
        }

        configId = rpushPlatformConfig.getId(); // 重新拿id

        // 先把子表所有值删掉，然后重建
        rpushPlatformConfigValueService.remove(Qw.newInstance(RpushPlatformConfigValue.class).eq("config_id", configId));

        List<RpushPlatformConfigValue> configValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            RpushPlatformConfigValue configValue = new RpushPlatformConfigValue();
            configValue.setConfigId(configId);
            configValue.setValue(value);
            configValue.setKey(key);
            configValue.setClientId(clientId);
            configValues.add(configValue);
        }
        rpushPlatformConfigValueService.saveBatch(configValues);
    }

    @Override
    public void setDefault(String configId, boolean defaultFlag) {
        if (StringUtils.isBlank(configId)) {
            return;
        }

        String clientId = SessionUtils.getClientId();
        RpushPlatformConfig rpushPlatformConfig = getOne(Qw.newInstance(RpushPlatformConfig.class).eq("id", configId).eq("client_id", clientId));
        if (rpushPlatformConfig == null) {
            return;
        }
        // 维护默认设置
        if (defaultFlag) {
            UpdateWrapper<RpushPlatformConfig> defaultFlagUw = new UpdateWrapper<>();
            defaultFlagUw.set("default_flag", false)
                    .eq("client_id", clientId)
                    .eq("platform", rpushPlatformConfig.getPlatform())
                    .eq("default_flag", true)
                    .ne("id", configId);
            update(defaultFlagUw);
        }
        rpushPlatformConfig.setDefaultFlag(defaultFlag);
        updateById(rpushPlatformConfig);
    }

    @Transactional
    @Override
    public void delete(Long configId) {
        if (configId == null) {
            return;
        }
        String clientId = SessionUtils.getClientId();
        remove(Qw.newInstance(RpushPlatformConfig.class).eq("id", configId).eq("client_id", clientId));
        rpushPlatformConfigValueService.remove(Qw.newInstance(RpushPlatformConfigValue.class).eq("config_id", configId).eq("client_id", clientId));
    }
}
