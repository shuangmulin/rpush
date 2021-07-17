package com.regent.rpush.route.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.api.route.RpushServerOnlineService;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.StatusCode;
import com.regent.rpush.dto.rpushserver.*;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.config.SessionUtils;
import com.regent.rpush.route.model.RpushServerOnline;
import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.service.IRpushServerOnlineService;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import com.regent.rpush.route.utils.Qw;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        ServerInfoDTO serverInfo = loginParam.getServerInfo();
        if (serverInfo == null) {
            throw new IllegalArgumentException("登录的服务端信息不全");
        }
        rpushServerOnline.setServerId(serverInfo.getHost() + ":" + serverInfo.getHttpPort());
        rpushServerOnline.setServerHost(serverInfo.getHost());
        rpushServerOnline.setServerHttpPort(serverInfo.getHttpPort());
        rpushServerOnline.setServerSocketPort(serverInfo.getSocketPort());
        rpushServerOnline.setClientId(rpushServerRegistration.getClientId());
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

    @PreAuthorize("hasAnyAuthority('push_message', 'admin')")
    @ApiOperation(value = "获取所有在线的设备信息")
    @PostMapping("/registrations")
    public ApiResult<Pagination<RpushServerRegistrationDTO>> onlineRegistrations(@RequestBody PageOnlineDTO param) {
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushServerOnline> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushServerOnline> qw = Qw.newInstance(RpushServerOnline.class).eq("client_id", SessionUtils.getClientId());
        if (param.getRegistrationId() != null) {
            qw.eq("registration_id", param.getRegistrationId());
        }
        page = (Page<RpushServerOnline>) rpushServerOnlineService.page(page, qw);

        List<RpushServerOnline> rpushServerOnlineList = page.getRecords();
        List<Long> onlineRegistrationIds = new ArrayList<>();
        for (RpushServerOnline rpushServerOnline : rpushServerOnlineList) {
            onlineRegistrationIds.add(rpushServerOnline.getRegistrationId());
        }

        if (onlineRegistrationIds.size() <= 0) {
            return ApiResult.of(new Pagination<>());
        }

        List<RpushServerRegistration> rpushServerRegistrations = rpushServerRegistrationService.list(Qw.newInstance(RpushServerRegistration.class).in("id", onlineRegistrationIds));
        List<RpushServerRegistrationDTO> result = new ArrayList<>();
        for (RpushServerRegistration rpushServerRegistration : rpushServerRegistrations) {
            RpushServerRegistrationDTO registrationDTO = new RpushServerRegistrationDTO();
            BeanUtil.copyProperties(rpushServerRegistration, registrationDTO);
            result.add(registrationDTO);
        }

        Pagination<RpushServerRegistrationDTO> pagination = new Pagination<>();
        pagination.setPageNum(pageNum);
        pagination.setPageSize(pageSize);
        pagination.setDataList(result);
        pagination.setTotal((int) page.getTotal());
        return ApiResult.of(pagination);
    }

}
