-- 1. 物流主题
  -- 1.1 运单相关统计
    -- 最近 1/7/30 日接单总数
    -- 最近 1/7/30 日接单金额
    -- 最近 1/7/30 日发单总数
    -- 最近 1/7/30 日发单金额
drop table if exists ads_trans_order_stats;
create table ads_trans_order_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近1天,7:最近7天,30:最近30天',
  `receive_order_count` bigint COMMENT '接单总数',
  `receive_order_amount` decimal(16,2) COMMENT '接单金额',
  `dispatch_order_count` bigint COMMENT '发单总数',
  `dispatch_order_amount` decimal(16,2) COMMENT '发单金额',
  PRIMARY KEY (`dt`, `recent_days`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '运单相关统计'
    ROW_FORMAT = DYNAMIC;

  -- 1.2 运输综合统计
    -- 最近 1/7/30 日完成运输次数
    -- 最近 1/7/30 日完成运输里程
    -- 最近 1/7/30 日完成运输时长
drop table if exists ads_trans_stats;
create table ads_trans_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近1天,7:最近7天,30:最近30天',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT ' 完成运输时长，单位：秒',
  PRIMARY KEY (`dt`, `recent_days`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '运输综合统计'
    ROW_FORMAT = DYNAMIC;

  -- 1.3 历史至今运单统计
    -- 历史至今运输中运单总数
    -- 历史至今运输中运单总金额
drop table if exists ads_trans_order_stats_td;
create table ads_trans_order_stats_td(
  `dt` date NOT NULL COMMENT '统计日期',
  `bounding_order_count` bigint COMMENT '运输中运单总数',
  `bounding_order_amount` decimal(16,2) COMMENT '运输中运单金额',
  PRIMARY KEY (`dt`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '历史至今运单统计'
    ROW_FORMAT = DYNAMIC;

-- 2. 运单主题
  -- 2.1 运单综合统计
    -- 最近 1/7/30 日下单数
    -- 最近 1/7/30 日下单金额
drop table if exists ads_order_stats;
create table ads_order_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `order_count` bigint COMMENT '下单数',
  `order_amount` decimal(16,2) COMMENT '下单金额',
  PRIMARY KEY (`dt`, `recent_days`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '运单综合统计'
    ROW_FORMAT = DYNAMIC;

  -- 2.2 各类型货物运单统计
    -- 最近 1/7/30 日各类型货物下单数
    -- 最近 1/7/30 日各类型货物下单金额
drop table if exists ads_order_cargo_type_stats;
create table ads_order_cargo_type_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `cargo_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '货物类型',
  `cargo_type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '货物类型名称',
  `order_count` bigint COMMENT '下单数',
  `order_amount` decimal(16,2) COMMENT '下单金额',
  PRIMARY KEY (`dt`, `recent_days`, `cargo_type`, `cargo_type_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '各类型货物运单统计'
    ROW_FORMAT = DYNAMIC;

-- 3. 城市机构主题
  -- 3.1 城市分析
    -- 最近 1/7/30 日各城市下单数
    -- 最近 1/7/30 日各城市下单金额
    -- 最近 1/7/30 日各城市完成运输次数
    -- 最近 1/7/30 日各城市完成运输里程
    -- 最近 1/7/30 日各城市完成运输时长
    -- 最近 1/7/30 日各城市平均每次运输时长
    -- 最近 1/7/30 日各城市平均每次运输里程
drop table if exists ads_city_stats;
create table ads_city_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` bigint COMMENT '最近天数,1:最近1天,7:最近7天,30:最近30天',
  `city_id` bigint COMMENT '城市ID',
  `city_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '城市名称',
  `order_count` bigint COMMENT '下单数',
  `order_amount` decimal COMMENT '下单金额',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `avg_trans_finish_distance` decimal(16,2) COMMENT '平均每次运输里程',
  `avg_trans_finish_dur_sec` bigint COMMENT '平均每次运输时长，单位：秒',
  PRIMARY KEY (`dt`, `recent_days`, `city_id`, `city_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '城市分析'
    ROW_FORMAT = DYNAMIC;

  -- 3.2 机构分析
    -- 最近 1/7/30 日各机构下单数
    -- 最近 1/7/30 日各机构下单金额
    -- 最近 1/7/30 日各机构完成运输次数
    -- 最近 1/7/30 日各机构完成运输里程
    -- 最近 1/7/30 日各机构完成运输时长
    -- 最近 1/7/30 日各机构平均每次运输时长
    -- 最近 1/7/30 日各机构平均每次运输里程
drop table if exists ads_org_stats;
create table ads_org_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近1天,7:最近7天,30:最近30天',
  `org_id` bigint COMMENT '机构ID',
  `org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '机构名称',
  `order_count` bigint COMMENT '下单数',
  `order_amount` decimal COMMENT '下单金额',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `avg_trans_finish_distance` decimal(16,2) COMMENT '平均每次运输里程',
  `avg_trans_finish_dur_sec` bigint COMMENT '平均每次运输时长，单位：秒',
  PRIMARY KEY (`dt`, `recent_days`, `org_id`, `org_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '机构分析'
    ROW_FORMAT = DYNAMIC;

-- 4. 线路班次主题
  -- 4.1 班次分析
    -- 最近 7/30 日各班次完成运输次数
    -- 最近 7/30 日各班次完成运输里程
    -- 最近 7/30 日各班次完成运输时长
    -- 最近 7/30 日各班次运输完成运单数
drop table if exists ads_shift_stats;
create table ads_shift_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,7:最近7天,30:最近30天',
  `shift_id` bigint COMMENT '班次ID',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `trans_finish_order_count` bigint COMMENT '运输完成运单数',
  PRIMARY KEY (`dt`, `recent_days`, `shift_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '班次分析'
    ROW_FORMAT = DYNAMIC;

  -- 4.2 线路分析
    -- 最近 7/30 日各线路完成运输次数
    -- 最近 7/30 日各班次完成运输里程
    -- 最近 7/30 日各线路完成运输时长
    -- 最近 7/30 日各线路运输完成运单数
drop table if exists ads_line_stats;
create table ads_line_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,7:最近7天,30:最近30天',
  `line_id` bigint COMMENT '线路ID',
  `line_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '线路名称',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `trans_finish_order_count` bigint COMMENT '运输完成运单数',
  PRIMARY KEY (`dt`, `recent_days`, `line_id`, `line_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '线路分析'
    ROW_FORMAT = DYNAMIC;

-- 5. 司机主题 仅统计第一司机
  -- 最近 7/30 日各司机运输次数
  -- 最近 7/30 日各司机运输里程
  -- 最近 7/30 日各司机总运输时长
  -- 最近 7/30 日各司机平均每次运输时长
  -- 最近 7/30 日各司机平均每次运输里程
  -- 最近 7/30 日各司机逾期次数
drop table if exists ads_driver_stats;
create table ads_driver_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,7:最近7天,30:最近30天',
  `driver_emp_id` bigint comment '第一司机员工ID',
  `driver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci comment '第一司机姓名',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `avg_trans_finish_distance` decimal(16,2) COMMENT '平均每次运输里程',
  `avg_trans_finish_dur_sec` bigint COMMENT '平均每次运输时长，单位：秒',
  `trans_finish_late_count` bigint COMMENT '逾期次数',
  PRIMARY KEY (`dt`, `recent_days`, `driver_emp_id`, `driver_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '司机分析'
    ROW_FORMAT = DYNAMIC;

-- 6. 卡车主题
  -- 最近 1/7/30 日各类卡车完成运输次数
  -- 最近 1/7/30 日各类卡车完成运输里程
  -- 最近 1/7/30 日各类卡车完成运输时长
  -- 最近 1/7/30 日各类卡车平均每次运输时长
  -- 最近 1/7/30 日各类卡车平均每次运输里程
drop table if exists ads_truck_stats;
create table ads_truck_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `truck_model_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '卡车类别编码',
  `truck_model_type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '卡车类别名称',
  `trans_finish_count` bigint COMMENT '完成运输次数',
  `trans_finish_distance` decimal(16,2) COMMENT '完成运输里程',
  `trans_finish_dur_sec` bigint COMMENT '完成运输时长，单位：秒',
  `avg_trans_finish_distance` decimal(16,2) COMMENT '平均每次运输里程',
  `avg_trans_finish_dur_sec` bigint COMMENT '平均每次运输时长，单位：秒',
  PRIMARY KEY (`dt`, `recent_days`, `truck_model_type`, `truck_model_type_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '卡车分析'
    ROW_FORMAT = DYNAMIC;

-- 7. 快递主题
  -- 7.1 快递综合统计
    -- 最近 1/7/30 日派送成功次数
    -- 最近 1/7/30 日分拣次数
drop table if exists ads_express_stats;
create table ads_express_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `deliver_suc_count` bigint COMMENT '派送成功次数（订单数）',
  `sort_count` bigint COMMENT '分拣次数',
  PRIMARY KEY (`dt`, `recent_days`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '快递综合统计'
    ROW_FORMAT = DYNAMIC;

  -- 7.2 各省份快递统计
    -- 最近 1/7/30 日各省份揽收次数
    -- 最近 1/7/30 日各省份揽收金额
    -- 最近 1/7/30 日各省份派送成功次数
    -- 最近 1/7/30 日各省份分拣次数
drop table if exists ads_express_province_stats;
create table ads_express_province_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `province_id` bigint COMMENT '省份ID',
  `province_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '省份名称',
  `receive_order_count` bigint COMMENT '揽收次数',
  `receive_order_amount` decimal(16,2) COMMENT '揽收金额',
  `deliver_suc_count` bigint COMMENT '派送成功次数',
  `sort_count` bigint COMMENT '分拣次数',
  PRIMARY KEY (`dt`, `recent_days`, `province_id`, `province_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '各省份快递统计'
    ROW_FORMAT = DYNAMIC;

  -- 7.3 各城市快递统计
    -- 最近 1/7/30 日各城市揽收次数
    -- 最近 1/7/30 日各城市揽收金额
    -- 最近 1/7/30 日各城市派送成功次数
    -- 最近 1/7/30 日各城市分拣次数
drop table if exists ads_express_city_stats;
create table ads_express_city_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `city_id` bigint COMMENT '城市ID',
  `city_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '城市名称',
  `receive_order_count` bigint COMMENT '揽收次数',
  `receive_order_amount` decimal(16,2) COMMENT '揽收金额',
  `deliver_suc_count` bigint COMMENT '派送成功次数',
  `sort_count` bigint COMMENT '分拣次数',
  PRIMARY KEY (`dt`, `recent_days`, `city_id`, `city_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '各城市快递统计'
    ROW_FORMAT = DYNAMIC;

  -- 7.4 各机构快递统计
    -- 最近 1/7/30 日各转运站揽收次数
    -- 最近 1/7/30 日各转运站揽收金额
    -- 最近 1/7/30 日各转运站派送成功次数
    -- 最近 1/7/30 日各机构分拣次数
drop table if exists ads_express_org_stats;
create table ads_express_org_stats(
  `dt` date NOT NULL COMMENT '统计日期',
  `recent_days` tinyint NOT NULL COMMENT '最近天数,1:最近天数,1:最近1天,7:最近7天,30:最近30天',
  `org_id` bigint COMMENT '机构ID',
  `org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '机构名称',
  `receive_order_count` bigint COMMENT '揽收次数',
  `receive_order_amount` decimal(16,2) COMMENT '揽收金额',
  `deliver_suc_count` bigint COMMENT '派送成功次数',
  `sort_count` bigint COMMENT '分拣次数',
  PRIMARY KEY (`dt`, `recent_days`, `org_id`, `org_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci comment '各机构快递统计'
    ROW_FORMAT = DYNAMIC;