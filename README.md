# rpush：多平台统一消息推送系统

* 一个接口触达多平台，支持一个接口多平台同时发送
* 消息平台逻辑与业务逻辑的解耦，业务方不需要关心各个平台的对接实现，只需要关心：要用哪些平台发、要发给对应平台的哪些人、要发什么内容
* 极强的扩展性，要新增一个消息平台的支持，理论只需要新增几个类就能完成，且不需要写任何前端代码即可获得该平台对应的ui交互（包括：配置交互、接收人维护、web手动消息发送交互等）。
* 当然支持web端手动发送消息
* 当然也支持定时任务
* 消息方案预设置
* 提供即时通讯实现，且支持服务器横向扩展
* 接收人导入
* 接收人按分组划分
* 消息日志
* ...

## 在线体验

http://159.75.121.163/
admin admin

## 目前支持的消息类型

* 邮箱
* 企业微信-应用消息
    * 文本消息
    * 图片消息
    * 视频消息
    * 文本消息
    * 文本卡片消息
    * 图文消息
    * Markdown消息
* 企业微信-群机器人
    * 文本消息
    * 图片消息
    * 图文消息
    * Markdown消息
* 微信公众号
    * 文本消息
    * 图文消息
    * 模板消息
* 钉钉-工作通知
    * 文本
    * Markdown
    * 链接消息
    * 卡片消息
    * OA消息

> Rpush的架构决定了扩展一个消息平台的消息类型会非常简单，所以如果要扩展一个消息平台，大部分时间都会花在查找该平台的对接文档上。后续会在工作之余加上其它的平台或消息类型。当然，欢迎参与扩展（扩展一个消息平台的消息类型，只需要几个java类即可，不需要写任何前端代码，即可获得包括ui交互内的所有功能）。

## 效果展示

#### 单个消息类型发送示例

<img alt="单个消息类型发送示例" src="https://image3.myjuniu.com/1d02b5b0c103403d8025852a3158161f_pro_b94f5ca5abfac91b1a8d729faf65d02f_single.gif">

#### web端多平台发送示例

<img alt="web端多平台发送示例" src="https://image3.myjuniu.com/1d02b5b0c103403d8025852a3158161f_pro_c3c95c7008390a1b2cb00d1cc1855fc7_web.gif">

#### postman多平台发送示例

<img alt="postman多平台发送示例" src="https://image3.myjuniu.com/1d02b5b0c103403d8025852a3158161f_pro_de79c4c1e47a026b60614b629f63040f_postman.gif">

#### 用代码发消息

秉持”业务服务只负责发消息“的解耦原则，业务服务在需要发消息的时候，代码应该越简单越好。所以，Rpush的发消息的sdk，一种消息只需要一行代码，有几种消息就有几行代码。比如这样：

```java
/**
 * @author shuangmulin
 * @since 2021/6/8/008 11:37
 **/
public class RpushSenderTest {
    /**
     * 要发送的内容
     */
    public static final String content = "您的会议室已经预定 \n" +
            ">**事项详情** \n" +
            ">事　项：开会\n" +
            ">组织者：@miglioguan \n" +
            ">参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang \n" +
            "> \n" +
            ">会议室：广州TIT 1楼 301\n" +
            ">日　期：2021年5月18日\n" +
            ">时　间：上午9:00-11:00\n" +
            "> \n" +
            ">请准时参加会议。 \n" +
            "> \n" +
            ">如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)";

    public static void main(String[] args) {
        // 企业微信-markdown消息
        MarkdownMessageDTO markdown = RpushMessage.WECHAT_WORK_AGENT_MARKDOWN().content(content).receiverIds(Collections.singletonList("ZhongBaoLin")).build();
        // 企业微信-群机器人消息
        TextMessageDTO text = RpushMessage.WECHAT_WORK_ROBOT_TEXT().content(content).receiverIds(Collections.singletonList("ZhongBaoLin")).build();
        // 邮箱
        EmailMessageDTO email = RpushMessage.EMAIL().title("会议通知").content(content).build();
        RpushService.instance("baolin", "666666").sendMessage(markdown, text, email); // 填上账号密码，运行即可
    }
}
```

以上代码，一次发送了三种不同平台的不同消息类型，全部代码加起来也只需要四五行代码而已。要获得以上效果，只需要maven引用rpush的sdk模块即可：

```xml

<project>
    <!-- 设置 jitpack.io 仓库 -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- 添加rpush-sdk依赖 -->
        <dependency>
            <groupId>com.github.shuangmulin.rpush</groupId>
            <artifactId>rpush-sdk</artifactId>
            <version>v1.0.2</version>
        </dependency>
    </dependencies>
</project>
```

#### 即时通讯

Rpush对即时通讯的实现方式比较`包容`，即对具体的连接实现做了解耦，不局限于某一种连接方式，可以netty，可以websocket，可以comet，当然也可以用原始的bio来做。这里展示websocke的网页端和netty实现的命令行客户端之间互相单聊和群聊的效果（该示例的相关代码：[客户端示例代码地址](https://github.com/shuangmulin/rpush-client-sample)）：
<img alt="单个消息类型发送示例" src="https://image3.myjuniu.com/1d02b5b0c103403d8025852a3158161f_pro_304939d3d7c870cc9bd1f04172385a87_%E5%8D%B3%E6%97%B6%E9%80%9A%E8%AE%AF%E6%95%88%E6%9E%9C.gif">

### 关于架构

// TODO...（时间问题只能先TODO）

### 一些比较核心的扩展点

// TODO...（时间问题只能先TODO）

## 用docker-compose快速部署一个Rpush服务

```yml
version: '2'
services:
  nginx:
    image: nginx
    container_name: nginx
    ports:
      - 80:80
    volumes:
      - /data/nginx/conf/nginx.conf:/etc/nginx/nginx.conf
      - /data/nginx/log:/var/log/nginx
      - /data/nginx/html:/usr/share/nginx/html
  rpush-eureka:
    image: shuangmulin/rpush-eureka
    container_name: rpush-eureka
    ports:
      - 8761:8761
  rpush-zuul:
    image: shuangmulin/rpush-zuul
    environment:
      - eureka-service-ip=172.16.0.11
      - eureka-service-port=8761
    container_name: rpush-zuul
    ports:
      - 8124:8124
  rpush-route:
    image: shuangmulin/rpush-route
    environment:
      - eureka-service-ip=localhost
      - eureka-service-port=8761
      - jdbc.url=jdbc:mysql://localhost:3306/rpush?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
      - jdbc.username=root
      - jdbc.password=123456
      - super-admin.username=superadmin
      - super-admin.password=superadmin
      - jwtSigningKey=fjksadjfklds
    container_name: rpush-route
    ports:
      - 8121:8121
  rpush-server:
    image: shuangmulin/rpush-server
    environment:
      - eureka-service-ip=localhost
      - eureka-service-port=8761
    container_name: rpush-server
    ports:
      - 8122:8122
  rpush-scheduler:
    image: shuangmulin/rpush-scheduler
    environment:
      - eureka-service-ip=localhost
      - eureka-service-port=8761
      - jdbc.url=jdbc:mysql://localhost:3306/rpush?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
      - jdbc.username=root
      - jdbc.password=123456
      - super-admin.username=superadmin
      - super-admin.password=superadmin
      - jwtSigningKey=fasdferear
    container_name: rpush-scheduler
    ports:
      - 8123:8123

```

> 运行 docker-compose up -d之后，直接访问8124端口即可
