package com.regent.rpush.common;

/**
 * Function:常量
 *
 * @author crossoverJie
 *         Date: 28/03/2018 17:41
 * @since JDK 1.8
 */
public class Constants {

    public static final String RPUSH_SERVER_NAME = "RPUSH-SERVER";

    /**
     * 自定义报文类型
     */
    public static class MessageType{
        /**
         * 登录
         */
        public static final int LOGIN = 1 ;
        /**
         * 业务消息
         */
        public static final int MSG = 2 ;

        /**
         * ping
         */
        public static final int PING = 3 ;
    }


}
