package com.regent.rpush.client;

/**
 * @author 钟宝林
 * @since 2021/2/25/025 13:14
 **/
public class App {

    private static final RpushClient rpushClient = new RpushClient();

    public static void start() throws Exception {
        rpushClient.start();
    }

    public static void reconnect() {
        rpushClient.reconnect();
    }

    public static void main(String[] args) throws Exception {
        start();
        System.out.println("结束");
    }

}
