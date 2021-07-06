package com.regent.rpush.route.service;

import com.regent.rpush.route.model.RpushServerRegistration;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 注册设备表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
public interface IRpushServerRegistrationService extends IService<RpushServerRegistration> {

    /**
     * 根据登录名称查询设备数据
     *
     * @param clientId  clientId
     * @param loginName 登录名称
     */
    RpushServerRegistration getByLoginName(String clientId, String loginName);

    /**
     * 根据设备id查询设备信息
     *
     * @param clientId       clientId
     * @param registrationId 设备id
     */
    RpushServerRegistration getByRegistrationId(String clientId, String registrationId);
}
