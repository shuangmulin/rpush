package com.regent.rpush.route.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rpush.api.route.RpushServerOnlineService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.StatusCode;
import com.regent.rpush.dto.rpushserver.LoginDTO;
import com.regent.rpush.dto.rpushserver.OfflineDTO;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import com.regent.rpush.route.model.RpushServerOnline;
import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.service.IRpushServerOnlineService;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 在线设备表，记录所有节点的在线设备 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
@RestController
@RequestMapping("/rpush-server-online")
public class RpushServerOnlineController implements RpushServerOnlineService {

    @Autowired
    private IRpushServerRegistrationService rpushServerRegistrationService;
    @Autowired
    private IRpushServerOnlineService rpushServerOnlineService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ApiResult<RpushServerRegistrationDTO> login(@RequestBody LoginDTO loginParam) {
        RpushServerRegistration rpushServerRegistration = rpushServerRegistrationService.getById(loginParam.getRegistrationId());
        if (rpushServerRegistration == null) {
            return ApiResult.of(StatusCode.VALIDATE_FAIL, "未注册的设备：" + loginParam.getRegistrationId());
        }
        offline(OfflineDTO.builder().registrationId(loginParam.getRegistrationId()).build()); // 先清一遍登录信息

        // 保存登录信息
        RpushServerOnline rpushServerOnline = new RpushServerOnline();
        rpushServerOnline.setRegistrationId(loginParam.getRegistrationId());
        rpushServerOnlineService.save(rpushServerOnline);
        RpushServerRegistrationDTO registrationDTO = new RpushServerRegistrationDTO();
        BeanUtil.copyProperties(rpushServerRegistration, registrationDTO);
        return ApiResult.of(registrationDTO);
    }

    @ApiOperation(value = "下线")
    @PostMapping("/offline")
    public ApiResult<String> offline(@RequestBody OfflineDTO offlineParam) {
        QueryWrapper<RpushServerOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("registration_id", offlineParam.getRegistrationId());
        rpushServerOnlineService.remove(wrapper);
        return new ApiResult<>();
    }

}
