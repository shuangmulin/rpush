CREATE TABLE IF NOT EXISTS `import_receiver`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `request_no`    varchar(32)  NOT NULL COMMENT '请求号',
    `platform`      varchar(32)  NOT NULL COMMENT '平台',
    `group_name`    varchar(32)  NOT NULL COMMENT '分组名称',
    `receiver_id`   varchar(255) NOT NULL COMMENT '接收人',
    `receiver_name` varchar(32)  NOT NULL DEFAULT '' COMMENT '接收人名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_group_id_receiver_id` (`request_no`,`group_name`,`receiver_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接收人导入表';

CREATE TABLE IF NOT EXISTS `oauth_access_token`
(
    `token_id`          varchar(255) DEFAULT NULL,
    `token`             longblob,
    `authentication_id` varchar(255) DEFAULT NULL,
    `user_name`         varchar(255) DEFAULT NULL,
    `client_id`         varchar(255) DEFAULT NULL,
    `authentication`    longblob,
    `refresh_token`     varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `oauth_refresh_token`
(
    `token_id`       varchar(255) DEFAULT NULL,
    `token`          longblob,
    `authentication` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `rpush_message_his`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `param`        longtext    NOT NULL COMMENT '参数json',
    `request_no`   varchar(32) NOT NULL COMMENT '请求号，唯一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_request_no` (`request_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=257 DEFAULT CHARSET=utf8mb4 COMMENT='消息历史记录表';

CREATE TABLE IF NOT EXISTS `rpush_message_his_detail`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `platform`     varchar(32)  NOT NULL COMMENT '平台',
    `message_type` varchar(50)  NOT NULL COMMENT '消息类型',
    `receiver_id`  varchar(255) NOT NULL COMMENT '接收人id',
    `config_id`    bigint(11) NOT NULL COMMENT '配置id',
    `config_name`  varchar(32)  NOT NULL DEFAULT '' COMMENT '配置名称（冗余配置，这个字段具有一定的时效性，具体配置可能会改）',
    `request_no`   varchar(32)  NOT NULL COMMENT '所属请求号，对应表rpush_message_his的字段request_no',
    `send_status`  int(11) NOT NULL DEFAULT '0' COMMENT '发送状态，0 未开始；1 发送成功；2 发送失败',
    `error_msg`    varchar(600) NOT NULL DEFAULT '发送成功' COMMENT '错误消息',
    PRIMARY KEY (`id`),
    KEY            `IDX_request_no` (`request_no`) USING HASH,
    KEY            `IDX_platform` (`platform`) USING HASH,
    KEY            `IDX_receiver_id` (`receiver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8mb4 COMMENT='消息历史记录详情表';

CREATE TABLE IF NOT EXISTS `rpush_message_scheme`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `param`        json        NOT NULL COMMENT '参数，json字符串',
    `platform`     varchar(32) NOT NULL COMMENT '所属平台',
    `message_type` varchar(50) NOT NULL COMMENT '所属消息类型',
    `name`         varchar(32) NOT NULL COMMENT '方案名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_message_type_name` (`message_type`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COMMENT='消息发送方案';

CREATE TABLE IF NOT EXISTS `rpush_platform_config`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `platform`     varchar(32) NOT NULL COMMENT '平台',
    `config_name`  varchar(32)          DEFAULT NULL COMMENT '配置的名称（显示用的字段）',
    `default_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认标识，0标识不是默认配置，1标识是默认配置',
    PRIMARY KEY (`id`),
    KEY            `UK_platform_config_name` (`platform`,`config_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='消息处理平台配置表';

CREATE TABLE IF NOT EXISTS `rpush_platform_config_value`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `config_id`    bigint(11) NOT NULL COMMENT '所属配置id',
    `key`          varchar(32)  NOT NULL COMMENT '参数键',
    `value`        varchar(225) NOT NULL DEFAULT '' COMMENT '参数值',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_config_id_key` (`config_id`,`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8mb4 COMMENT='消息处理平台配置对应的具体参数值';

CREATE TABLE IF NOT EXISTS `rpush_scheduler_task`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT,
    `date_created`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated`    datetime          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `job_name`        varchar(32)       DEFAULT NULL COMMENT '任务名',
    `description`     varchar(255)      DEFAULT NULL COMMENT '任务描述',
    `cron_expression` varchar(255)      DEFAULT NULL COMMENT 'cron表达式',
    `bean_class`      varchar(255)      DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
    `enable_flag`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用标志，true表示已启用，false表示未启用',
    `job_group`       varchar(32)       DEFAULT NULL COMMENT '任务分组',
    `param`           json     NOT NULL COMMENT '附带的参数，json格式',
    `start_at`        datetime NOT NULL,
    `end_at`          datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `job_name_job_group` (`job_name`,`job_group`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COMMENT='调度任务表';

CREATE TABLE IF NOT EXISTS `rpush_server_online`
(
    `id`                 bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`            int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated`       datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `registration_id`    bigint(20) NOT NULL COMMENT '注册设备id',
    `server_id`          varchar(32) NOT NULL,
    `server_host`        varchar(32) NOT NULL,
    `server_http_port`   int(11) NOT NULL,
    `server_socket_port` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY                  `registration_id` (`registration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='在线设备表，记录所有节点的在线设备';

CREATE TABLE IF NOT EXISTS `rpush_server_registration`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `name`         varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1363049405430763523 DEFAULT CHARSET=utf8mb4 COMMENT='注册设备表';

CREATE TABLE IF NOT EXISTS `rpush_template`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`           int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated`      datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `platform`          varchar(32)  NOT NULL COMMENT '平台',
    `template_name`     varchar(255) NOT NULL DEFAULT '' COMMENT '模板名称',
    `title`             varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板标题',
    `content`           varchar(255) NOT NULL COMMENT '模板内容',
    `receiver_ids`      varchar(255) NOT NULL DEFAULT '' COMMENT '预设接收人，多个用;隔开。（对应的是rpush_template_receiver表的receiver_id）',
    `receiver_group_id` bigint(11) DEFAULT NULL COMMENT '接收人分组id，可以为空（rpush_template_receiver_group表的id）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_platform_template_name` (`platform`,`template_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

CREATE TABLE IF NOT EXISTS `rpush_template_receiver`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`       int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated`  datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `platform`      varchar(32)  NOT NULL COMMENT '平台',
    `group_id`      bigint(11) NOT NULL COMMENT '所属分组id',
    `receiver_id`   varchar(255) NOT NULL COMMENT '接收人，用来发消息的id性质的字段，如果是邮箱就是要发送的邮箱，如果是企业微信就是企业微信对应用户的id，如果是rpush就是registrationId',
    `receiver_name` varchar(32)           DEFAULT '' COMMENT '接收人名称，显示用的字段，可以为空',
    `profile_photo` varchar(355)          DEFAULT '' COMMENT '头像，显示用的字段，可以为空',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_group_id_receiver_id` (`platform`,`group_id`,`receiver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='消息模板-预设接收人表';

CREATE TABLE IF NOT EXISTS `rpush_template_receiver_group`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `version`      int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
    `date_created` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `date_updated` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `platform`     varchar(32) NOT NULL COMMENT '所属平台',
    `group_name`   varchar(32)          DEFAULT '' COMMENT '分组名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_platform_group_name` (`platform`,`group_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='消息模板-接收人分组表';
