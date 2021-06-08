package com.regent.rpush.route.controller;


import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.StatusCode;
import com.regent.rpush.dto.common.IdAndName;
import com.regent.rpush.dto.common.IdStrAndName;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.route.sheme.SchemeDTO;
import com.regent.rpush.dto.route.sheme.SchemeFieldVO;
import com.regent.rpush.route.model.RpushMessageScheme;
import com.regent.rpush.route.service.IRpushMessageSchemeService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import com.regent.rpush.route.utils.Qw;
import com.regent.rpush.route.config.SessionUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 消息发送方案 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-04-05
 */
@RestController
@RequestMapping("/rpush-message-scheme")
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushMessageSchemeController {

    @Autowired
    private IRpushMessageSchemeService rpushMessageSchemeService;

    @ApiOperation("获取方案所有字段")
    @GetMapping("/field")
    public ApiResult<List<SchemeFieldVO>> field(@NotNull(message = "未知消息类型") MessageType messageType) {
        return ApiResult.of(MessageHandlerUtils.listSchemeField(messageType));
    }

    @ApiOperation("获取平台所有消息类型")
    @GetMapping("/type")
    public ApiResult<List<IdStrAndName>> allType(@NotNull(message = "未知平台") MessagePlatformEnum platform) {
        List<IdStrAndName> results = new ArrayList<>();

        MessageType[] values = MessageType.values();
        for (MessageType value : values) {
            if (!value.getPlatform().equals(platform)) {
                continue;
            }
            results.add(new IdStrAndName(value.name(), value.getName()));
        }
        return ApiResult.of(results);
    }

    @ApiOperation("更新或保存方案")
    @PostMapping("/update")
    public ApiResult<RpushMessageScheme> update(@NotNull(message = "参数不全") @RequestBody SchemeDTO scheme) {
        return ApiResult.of(rpushMessageSchemeService.saveOrUpdate(scheme));
    }

    @ApiOperation("删除方案")
    @DeleteMapping
    public ApiResult<Boolean> delete(String schemeId) {
        if (StringUtils.isBlank(schemeId)) {
            return ApiResult.of(StatusCode.VALIDATE_FAIL, "未知方案");
        }
        rpushMessageSchemeService.delete(schemeId);
        return ApiResult.success();
    }

    @ApiOperation("查询方案列表")
    @GetMapping("/list")
    public ApiResult<List<IdAndName>> listScheme(@NotBlank(message = "未知消息类型") MessageType messageType) {
        return ApiResult.of(rpushMessageSchemeService.listScheme(messageType));
    }

    @ApiOperation("查询某个方案")
    @GetMapping("/detail")
    public ApiResult<RpushMessageScheme> getSchemeParam(@NotBlank(message = "未知消息") String schemeId) {
        RpushMessageScheme scheme = rpushMessageSchemeService.getOne(Qw.newInstance(RpushMessageScheme.class).eq("client_id", SessionUtils.getClientId()).eq("id", schemeId));
        return ApiResult.of(scheme);
    }

}
