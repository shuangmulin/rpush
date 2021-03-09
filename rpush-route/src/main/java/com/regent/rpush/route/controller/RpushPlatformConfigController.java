package com.regent.rpush.route.controller;


import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.route.PlatformDTO;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.dto.route.config.UpdateConfigDTO;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
public class RpushPlatformConfigController {

    @Autowired
    private IRpushPlatformConfigService rpushPlatformConfigService;

    @ApiOperation(value = "获取所有平台")
    @GetMapping("/platform")
    public ApiResult<List<PlatformDTO>> platforms(String keyword) {
        List<PlatformDTO> platforms = new ArrayList<>();
        for (MessagePlatformEnum platformEnum : MessagePlatformEnum.values()) {
            String name = platformEnum.getName();
            if (StringUtils.isNotBlank(keyword) && !name.contains(keyword)) {
                continue;
            }
            platforms.add(PlatformDTO.builder()
                    .id(platformEnum.name())
                    .name(platformEnum.getName())
                    .description(platformEnum.getDescription())
                    .enable(platformEnum.isEnable())
                    .build());
        }
        return ApiResult.of(platforms);
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
    public ApiResult<Config> getConfig(@PathVariable("platform") MessagePlatformEnum platform,
                                       @PathVariable("configId") Long configId) {
        if (configId == null || platform == null) {
            return ApiResult.of(null);
        }
        ConfigTableDTO configTableDTO = rpushPlatformConfigService.pageConfig(platform, configId, null, null, null);
        if (configTableDTO == null) {
            return ApiResult.of(null);
        }
        Pagination<Config> pagination = configTableDTO.getPagination();
        if (pagination == null || pagination.getDataList() == null || pagination.getDataList().size() <= 0) {
            return ApiResult.of(null);
        }
        Config table = pagination.getDataList().get(0);
        return ApiResult.of(table);
    }

    @ApiOperation("获取某个平台的配置字段")
    @GetMapping("/{platform}/config/field")
    public ApiResult<List<ConfigFieldVO>> configField(@PathVariable("platform") MessagePlatformEnum platform) {
        return ApiResult.of(MessageHandlerUtils.listConfigFieldName(platform));
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

    @ApiOperation("设为默认")
    @DeleteMapping("/config/{configId}")
    public ApiResult<String> delete(@PathVariable("configId") Long configId) {
        rpushPlatformConfigService.delete(configId);
        return ApiResult.success();
    }

}
