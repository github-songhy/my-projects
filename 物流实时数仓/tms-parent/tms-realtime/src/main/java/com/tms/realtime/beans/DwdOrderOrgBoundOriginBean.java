package com.tms.realtime.beans;

import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/28
 * 中转实体类
 */
@Data
public class DwdOrderOrgBoundOriginBean {
    // 编号（主键）
    String id;

    // 运单编号
    String orderId;

    // 机构id
    String orgId;

    // 状态 出库 入库
    String status;

    // 入库时间
    String inboundTime;

    // 入库人员id
    String inboundEmpId;

    // 分拣时间
    String sortTime;

    // 分拣人员id
    String sorterEmpId;

    // 出库时间
    String outboundTime;

    // 出库人员id
    String outboundEmpId;

    // 创建时间
    String createTime;

    // 修改时间
    String updateTime;

    // 删除标志
    String isDeleted;
}
