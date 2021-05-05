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

## 效果展示
#### 单个消息类型发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/%E5%8D%95%E4%B8%AA%E6%B6%88%E6%81%AF%E7%B1%BB%E5%9E%8B%E5%8F%91%E9%80%81.gif?raw=true">

#### web端多平台发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/web%E7%AB%AF%E5%A4%9A%E5%B9%B3%E5%8F%B0%E5%8F%91%E9%80%81.gif?raw=true">

#### postman多平台发送示例
<img alt="treeexcel" src="https://github.com/shuangmulin/static/blob/master/rpush/postman%E5%A4%9A%E5%B9%B3%E5%8F%B0%E5%8F%91%E9%80%81.gif?raw=true">
