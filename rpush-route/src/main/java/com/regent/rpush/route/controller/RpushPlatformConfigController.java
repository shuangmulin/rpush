package com.regent.rpush.route.controller;


import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.PlatformDTO;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigTableDTO;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{platform}/config")
    public ApiResult<ConfigTableDTO> pageConfig(@PathVariable("platform") MessagePlatformEnum platform,
                                                Integer pageNum,
                                                Integer pageSize) {
        ConfigTableDTO table = rpushPlatformConfigService.pageConfig(platform, pageNum, pageSize);
        return ApiResult.of(table);
    }

    @GetMapping("/{platform}/config/field")
    public ApiResult<List<ConfigFieldVO>> configField(@PathVariable("platform") MessagePlatformEnum platform) {
        return ApiResult.of(MessageHandlerUtils.listConfigFieldName(platform));
    }

}
