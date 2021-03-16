package com.regent;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

/**
 * @author 钟宝林
 * @since 2021/3/15/015 21:19
 **/
public class Test {
    public static void main(String[] args) throws WxErrorException {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId("ww3ed3e8dacb057641");      // 设置微信企业号的appid
        config.setCorpSecret("QBNhqnQrdaqO2x2UZsR3e4hIm2n0zdy4I5FhnzZXeSE");  // 设置微信企业号的app corpSecret
        config.setAgentId(1000002);     // 设置微信企业号应用ID

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);

        String userId = "ZhongBaoLin";
        WxCpMessage message = WxCpMessage.TEXT().agentId(1000002).toUser(userId).content("Hello World").build();
        wxCpService.getMessageService().send(message);
    }

}
