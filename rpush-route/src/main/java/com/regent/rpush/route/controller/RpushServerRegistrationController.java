package com.regent.rpush.route.controller;


import com.regent.rpush.api.route.RpushServerRegistrationService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.RegisterDTO;
import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
