package com.regent.rpush.route.controller;


import cn.hutool.core.bean.BeanUtil;
import com.regent.rpush.api.route.RpushServerRegistrationService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.RegisterDTO;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 注册设备表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
@RestController
@RequestMapping("/rpush-server-registration")
public class RpushServerRegistrationController implements RpushServerRegistrationService {

    @Autowired
    private IRpushServerRegistrationService rpushServerRegistrationService;

    @ApiOperation(value = "设备注册")
    @PostMapping
    public ApiResult<String> register(@RequestBody RegisterDTO registerParam) {
        RpushServerRegistration rpushServerRegistration = new RpushServerRegistration();
        rpushServerRegistration.setName(registerParam.getName());
        rpushServerRegistrationService.save(rpushServerRegistration);
        return new ApiResult<>();
    }

    @PreAuthorize("hasAnyAuthority('push_message', 'admin')")
    @ApiOperation(value = "获取某个设备信息")
    @GetMapping("/{id}")
    public ApiResult<RpushServerRegistrationDTO> getRegistration(@PathVariable("id") Long registrationId) {
        RpushServerRegistration byId = rpushServerRegistrationService.getById(registrationId);
        RpushServerRegistrationDTO registrationDTO = new RpushServerRegistrationDTO();
        BeanUtil.copyProperties(byId, registrationDTO);
        return ApiResult.of(registrationDTO);
    }

}
