package com.regent.rpush.api.route;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.LoginDTO;
import com.regent.rpush.dto.rpushserver.OfflineDTO;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 钟宝林
 * @since 2021/2/20/020 17:32
 **/
@FeignClient(name = "rpush-route", contextId = "RpushServerOnlineService")
@RequestMapping("/rpush-server-online")
public interface RpushServerOnlineService {

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    ApiResult<RpushServerRegistrationDTO> login(@RequestBody LoginDTO loginParam);

    @ApiOperation(value = "下线")
    @PostMapping("/offline")
    ApiResult<String> offline(@RequestBody OfflineDTO offlineParam);

}
