package com.regent.rpush.api.route;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.RegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 钟宝林
 * @since 2021/2/20/020 17:45
 **/
@FeignClient(name = "rpush-route", contextId = "RpushServerRegistrationService")
@RequestMapping("/rpush-server-registration")
public interface RpushServerRegistrationService {

    @PostMapping
    ApiResult<String> register(@RequestBody RegisterDTO registerParam);

}
