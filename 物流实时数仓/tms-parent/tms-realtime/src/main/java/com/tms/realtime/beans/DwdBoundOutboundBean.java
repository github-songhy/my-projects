package com.tms.realtime.beans;

import lombok.Builder;
import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/28
 * 中转域:出库实体类
 */
@Data
@Builder
public class DwdBoundOutboundBean {
    // 编号（主键）
    String id;

    // 运单编号
    String orderId;

    // 机构id
    String orgId;

    // 出库时间
    String outboundTime;

    // 出库人员id
    String outboundEmpId;

    // 时间戳
    Long ts;
}

