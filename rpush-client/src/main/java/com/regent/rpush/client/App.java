package com.regent.rpush.client;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 13:14
 **/
public class App {

    public static void main(String[] args) throws Exception {
        RpushClient rpushClient = new RpushClient();
        rpushClient.start();
    }

}
