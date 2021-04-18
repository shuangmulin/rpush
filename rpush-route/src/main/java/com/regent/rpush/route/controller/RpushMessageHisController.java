package com.regent.rpush.route.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息历史记录表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-16
 */
@RestController
@RequestMapping("/rpush-message-his")
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushMessageHisController {

}
