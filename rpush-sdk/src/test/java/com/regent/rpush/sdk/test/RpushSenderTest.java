package com.regent.rpush.sdk.test;

import com.regent.rpush.dto.message.wechatwork.agent.MarkdownMessageDTO;
import com.regent.rpush.dto.message.wechatwork.robot.TextMessageDTO;
import com.regent.rpush.sdk.RpushMessage;
import com.regent.rpush.sdk.RpushService;

import java.util.Collections;

/**
 * @author 钟宝林
 * @since 2021/6/8/008 11:37
 **/
public class RpushSenderTest {

    public static void main(String[] args) {
        String content = "您的会议室已经预定 \n" +
                ">**事项详情** \n" +
                ">事　项：开会\n" +
                ">组织者：@miglioguan \n" +
                ">参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang \n" +
                "> \n" +
                ">会议室：广州TIT 1楼 301\n" +
                ">日　期：2018年5月18日\n" +
                ">时　间：上午9:00-11:00\n" +
                "> \n" +
                ">请准时参加会议。 \n" +
                "> \n" +
                ">如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)";
        MarkdownMessageDTO markdown = RpushMessage.WECHAT_WORK_AGENT_MARKDOWN().content(content).receiverIds(Collections.singletonList("ZhongBaoLin")).build();
        TextMessageDTO text = RpushMessage.WECHAT_WORK_ROBOT_TEXT().content(content).receiverIds(Collections.singletonList("ZhongBaoLin")).build();
//        RpushSender.instance( "http://localhost:8124", "baolin", "666666").sendMessage(markdown, text);
        RpushService.instance("baolin", "666666").sendMessage(markdown, text);
    }

}
