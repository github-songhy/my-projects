package com.tms.realtime.beans;

import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/26
 */
@Data
public class TmsConfigDimBean {
    // 数据源表表名
    String sourceTable;

    // 目标表表名
    String sinkTable;

    // 目标表表名
    String sinkFamily;

    // 需要的字段列表
    String sinkColumns;

    // 需要的主键
    String sinkPk;

    // 关联外键列表
    String foreignKeys;
}

