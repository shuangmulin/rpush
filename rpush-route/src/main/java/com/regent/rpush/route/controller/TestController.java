package com.regent.rpush.route.controller;

import com.regent.rpush.route.utils.sdk.SdkGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 钟宝林
 * @since 2021/6/8/008 14:24
 **/
@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    private SdkGenerator sdkGenerator;

    @GetMapping("/sdk")
    public String sdk() {
        return sdkGenerator.generate();
    }
}
