package com.regent.rpush.route.controller;


import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.common.IdStrAndName;
import com.regent.rpush.dto.enumration.ConfigValueType;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.route.PlatformDTO;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.dto.route.config.UpdateConfigDTO;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.service.IRpushTemplateService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import com.regent.rpush.route.utils.Qw;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息处理平台配置表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
@RestController
@RequestMapping("/rpush-platform-config")
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushPlatformConfigController {

    @Autowired
    private IRpushPlatformConfigService rpushPlatformConfigService;
    @Autowired
    private IRpushTemplateService rpushTemplateService;

    @ApiOperation(value = "获取所有平台")
    @GetMapping("/platform")
    public ApiResult<List<PlatformDTO>> platforms(String keyword) {
        List<PlatformDTO> platforms = new ArrayList<>();
        for (MessagePlatformEnum platformEnum : MessagePlatformEnum.values()) {
            if (!platformEnum.isEnable()) {
                continue;
            }
            String name = platformEnum.getName();
            if (StringUtils.isNotBlank(keyword) && !name.contains(keyword)) {
                continue;
            }
            platforms.add(PlatformDTO.builder()
                    .id(platformEnum.name())
                    .name(platformEnum.getName())
                    .description(platformEnum.getDescription())
                    .validateReg(platformEnum.getValidateReg())
                    .build());
        }
        return ApiResult.of(platforms);
    }

    @ApiOperation(value = "获取所有消息类型")
    @GetMapping("/message-type")
    public ApiResult<List<IdStrAndName>> messageTypes(String keyword) {
        List<IdStrAndName> messageTypes = new ArrayList<>();
        for (MessageType messageType : MessageType.values()) {
            String name = messageType.getName();
            if (StringUtils.isNotBlank(keyword) && !name.contains(keyword)) {
                continue;
            }
            messageTypes.add(IdStrAndName.builder()
                    .id(messageType.name())
                    .name(messageType.getPlatform().getName() + "-" + messageType.getName())
                    .build());
        }
        return ApiResult.of(messageTypes);
    }

    @ApiOperation("平台配置的列表查询")
    @GetMapping("/{platform}/config")
    public ApiResult<ConfigTableDTO> pageConfig(@PathVariable("platform") MessagePlatformEnum platform,
                                                String configName,
                                                Integer pageNum,
                                                Integer pageSize) {
        ConfigTableDTO table = rpushPlatformConfigService.pageConfig(platform, null, configName, pageNum, pageSize);
        return ApiResult.of(table);
    }

    @ApiOperation("获取某个配置的数据")
    @GetMapping("/{platform}/config/{configId}")
    public ApiResult<Map<String, Object>> getConfig(@PathVariable("platform") MessagePlatformEnum platform,
                                                    @PathVariable("configId") Long configId) {
        if (configId == null || platform == null) {
            return ApiResult.of(null);
        }
        ConfigTableDTO configTableDTO = rpushPlatformConfigService.pageConfig(platform, configId, null, null, null);
        if (configTableDTO == null) {
            return ApiResult.of(null);
        }
        Pagination<Map<String, Object>> pagination = configTableDTO.getPagination();
        if (pagination == null || pagination.getDataList() == null || pagination.getDataList().size() <= 0) {
            return ApiResult.of(null);
        }
        return ApiResult.of(pagination.getDataList().get(0));
    }

    @ApiOperation("获取某个平台的配置字段")
    @GetMapping("/{platform}/config/field")
    public ApiResult<List<ConfigFieldVO>> configField(@PathVariable("platform") MessagePlatformEnum platform) {
        List<ConfigFieldVO> configFieldVOS = MessageHandlerUtils.listConfigFieldName(platform);
        for (ConfigFieldVO configFieldVO : configFieldVOS) {
            ConfigValueType type = configFieldVO.getType();
            switch (type) {
                case RPUSH_TEMPLATE:
                    List<RpushTemplate> rpushTemplates = rpushTemplateService.list(Qw.newInstance(RpushTemplate.class).eq("platform", platform));
                    List<IdStrAndName> options = new ArrayList<>();
                    for (RpushTemplate rpushTemplate : rpushTemplates) {
                        options.add(IdStrAndName.builder().id(String.valueOf(rpushTemplate.getId())).name(rpushTemplate.getTemplateName()).build());
                    }
                    configFieldVO.setOptions(options);
                    break;
            }
        }
        return ApiResult.of(configFieldVOS);
    }

    @ApiOperation("更新配置")
    @PostMapping("/config")
    public ApiResult<String> updateConfig(@Valid @RequestBody UpdateConfigDTO updateConfigDTO) {
        rpushPlatformConfigService.updateConfig(updateConfigDTO);
        return ApiResult.success();
    }

    @ApiOperation("设为默认")
    @GetMapping("/setDefault")
    public ApiResult<String> setDefault(String configId, boolean defaultFlag) {
        rpushPlatformConfigService.setDefault(configId, defaultFlag);
        return ApiResult.success();
    }

    @ApiOperation("删除配置")
    @DeleteMapping("/config/{configId}")
    public ApiResult<String> delete(@PathVariable("configId") Long configId) {
        rpushPlatformConfigService.delete(configId);
        return ApiResult.success();
    }

}
