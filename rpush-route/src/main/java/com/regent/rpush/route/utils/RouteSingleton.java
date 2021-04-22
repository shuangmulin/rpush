package com.regent.rpush.route.utils;

import com.regent.rpush.common.SingletonUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例集合
 *
 * @author 钟宝林
 * @since 2021/4/19/019 23:06
 **/
public class RouteSingleton {

    public static WxCpServiceImpl wxCpService(String corpId, String secret, Integer agentId) {
        return SingletonUtil.get(corpId + secret + agentId, () -> {
            WxCpDefaultConfigImpl cpConfig = new WxCpDefaultConfigImpl();
            cpConfig.setCorpId(corpId);
            cpConfig.setCorpSecret(secret);
            cpConfig.setAgentId(agentId);
            WxCpServiceImpl wxCpService1 = new WxCpServiceImpl();
            wxCpService1.setWxCpConfigStorage(cpConfig);
            return wxCpService1;
        });
    }

    public static List<WxCpDepart> wxCpDepartments(String corpId, String secret, Integer agentId, Long departmentId, boolean forceRefresh) {
        return SingletonUtil.get("WxCpDepart" + corpId + secret + agentId + departmentId, () -> {
            try {
                return RouteSingleton.wxCpService(corpId, secret, agentId).getDepartmentService().list(departmentId);
            } catch (WxErrorException e) {
                throw new IllegalStateException(e);
            }
        }, forceRefresh);
    }

    public static List<WxCpDepart> wxCpDepartmentsByIds(String corpId, String secret, Integer agentId, List<Long> departmentIds, boolean forceRefresh) {
        return SingletonUtil.get("WxCpDepart" + corpId + secret + agentId + departmentIds.toString(), () -> {
            try {
                List<WxCpDepart> departs = new ArrayList<>();
                for (Long departmentId : departmentIds) {
                    departs.addAll(RouteSingleton.wxCpService(corpId, secret, agentId).getDepartmentService().list(departmentId));
                }
                return departs;
            } catch (WxErrorException e) {
                throw new IllegalStateException(e);
            }
        }, forceRefresh);
    }

}
