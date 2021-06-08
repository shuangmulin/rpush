package com.regent.rpush.route.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.route.dto.ReceiverBatchInsertDTO;
import com.regent.rpush.route.mapper.RpushTemplateReceiverMapper;
import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.utils.Qw;
import com.regent.rpush.route.config.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 消息模板-预设接收人表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@Service
public class RpushTemplateReceiverServiceImpl extends ServiceImpl<RpushTemplateReceiverMapper, RpushTemplateReceiver> implements IRpushTemplateReceiverService {

    @Autowired
    private RpushTemplateReceiverMapper rpushTemplateReceiverMapper;

    @Override
    public void updateReceiver(RpushTemplateReceiver receiver) {
        String clientId = SessionUtils.getClientId();
        String receiverId = receiver.getReceiverId();
        Long id = receiver.getId();
        MessagePlatformEnum platform = MessagePlatformEnum.valueOf(receiver.getPlatform());
        platform.matcherThrow(receiverId); // 验证格式
        if (StringUtils.isNotBlank(receiverId)) {
            // id判重
            QueryWrapper<RpushTemplateReceiver> receiverNameQw = Qw.newInstance(RpushTemplateReceiver.class)
                    .eq("client_id", clientId)
                    .eq("group_id", receiver.getGroupId())
                    .eq("receiver_id", receiverId);
            if (id != null) {
                receiverNameQw.ne("id", id);
            }
            RpushTemplateReceiver existReceiver = getOne(receiverNameQw);
            Assert.isTrue(existReceiver == null, "接收人重复");
        }

        // 入库
        if (id == null) {
            receiver.setClientId(clientId);
            save(receiver);
        } else {
            updateById(receiver);
        }
    }

    @Override
    public void batchInsert(MessagePlatformEnum platform, List<ReceiverBatchInsertDTO> receivers) {
        String clientId = SessionUtils.getClientId();
        String requestNo = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            StringBuilder insertSql = new StringBuilder();
            insertSql.append(" INSERT INTO import_receiver (request_no, platform, receiver_id, receiver_name, group_name, client_id) VALUES ");
            List<String> insertSqlItems = new ArrayList<>(receivers.size());
            for (ReceiverBatchInsertDTO receiver : receivers) {
                if (StringUtils.isBlank(receiver.getReceiverId()) || StringUtils.isBlank(receiver.getReceiverName())) {
                    continue;
                }

                List<String> receiverFields = new ArrayList<>();
                receiverFields.add("'" + requestNo + "'");
                receiverFields.add("'" + platform.name() + "'");
                receiverFields.add("'" + receiver.getReceiverId() + "'");
                receiverFields.add("'" + receiver.getReceiverName() + "'");
                String receiverGroupName = receiver.getReceiverGroupName();
                receiverGroupName = StringUtils.isBlank(receiverGroupName) ? "默认分组" : receiverGroupName;
                receiverFields.add("'" + receiverGroupName + "'");
                receiverFields.add("'" + clientId + "'");
                insertSqlItems.add("(" + CollUtil.join(receiverFields, ",") + ")");
            }
            if (insertSqlItems.size() <= 0) {
                throw new IllegalArgumentException("空数据Excel");
            }
            insertSql.append(CollUtil.join(insertSqlItems, ","));
            insertSql.append(" ON DUPLICATE KEY UPDATE receiver_name = receiver_name;");
            rpushTemplateReceiverMapper.execute(insertSql.toString());

            // 导入分组
            String sqlSb;
            sqlSb = " INSERT INTO rpush_template_receiver_group (platform, group_name, client_id) SELECT\n" +
                    "  *\n" +
                    " FROM\n" +
                    "  (\n" +
                    "   SELECT\n" +
                    "    ir.platform AS platform,\n" +
                    "    ir.group_name AS group_name,\n" +
                    "    ir.client_id AS client_id\n" +
                    "   FROM\n" +
                    "    import_receiver AS ir\n" +
                    "   WHERE\n" +
                    "    ir.request_no = '" + requestNo + "'\n" +
                    "   GROUP BY\n" +
                    "    ir.request_no,\n" +
                    "    ir.platform,\n" +
                    "    ir.group_name\n" +
                    "  ) AS t ON DUPLICATE KEY UPDATE platform = t.platform;\n";
            rpushTemplateReceiverMapper.execute(sqlSb);

            // 导入接收人
            sqlSb = " INSERT INTO rpush_template_receiver (\n" +
                    "  platform,\n" +
                    "  group_id,\n" +
                    "  receiver_id,\n" +
                    "  receiver_name,\n" +
                    "  client_id\n" +
                    " ) SELECT\n" +
                    "  *\n" +
                    " FROM\n" +
                    "  (\n" +
                    "   SELECT\n" +
                    "    ir.platform AS platform,\n" +
                    "    r.id AS group_id,\n" +
                    "    ir.receiver_id AS receiver_id,\n" +
                    "    ir.receiver_name AS receiver_name,\n" +
                    "    ir.client_id AS client_id\n" +
                    "   FROM\n" +
                    "    import_receiver AS ir\n" +
                    "   INNER JOIN rpush_template_receiver_group AS r ON r.platform = ir.platform\n" +
                    "   AND r.group_name = ir.group_name\n" +
                    "   WHERE\n" +
                    "    ir.request_no = '" + requestNo + "'\n" +
                    "  ) AS t ON DUPLICATE KEY UPDATE platform = t.platform;";
            rpushTemplateReceiverMapper.execute(sqlSb);
        } finally {
            rpushTemplateReceiverMapper.execute("delete from import_receiver where request_no = '" + requestNo + "'");
        }
    }

    @Override
    public void delete(Long id) {
        remove(Qw.newInstance(RpushTemplateReceiver.class).eq("id", id).eq("client_id", SessionUtils.getClientId()));
    }
}
