# rpush：多平台统一消息系统
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

> Rpush的架构决定了扩展一个消息平台的消息类型会非常简单，所以如果要扩展一个消息平台，大部分时间都会花在查找该平台的对接文档上。如果有需要，可以提Issues，我尽量在工作之余加上相应的平台或消息类型。当然，您也可以自行扩展（再重复一下，扩展一个消息平台的消息类型，只需要几个java类即可，不需要写任何前端代码，即可获得包括ui交互内的所有功能）。

## 效果展示
#### 单个消息类型发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/%E5%8D%95%E4%B8%AA%E6%B6%88%E6%81%AF%E7%B1%BB%E5%9E%8B%E5%8F%91%E9%80%81.gif?raw=true">

#### web端多平台发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/web%E7%AB%AF%E5%A4%9A%E5%B9%B3%E5%8F%B0%E5%8F%91%E9%80%81.gif?raw=true">

#### postman多平台发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/postman%E5%A4%9A%E5%B9%B3%E5%8F%B0%E5%8F%91%E9%80%81.gif?raw=true">
