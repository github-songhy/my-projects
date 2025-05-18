package com.tms.realtime.beans;

import lombok.Builder;
import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/29
 * 中转域：机构粒度分拣业务过程聚合统计实体类
 */
@Data
@Builder
public class DwsBoundOrgSortDayBean {
    // 统计日期
    String curDate;

    // 机构 ID
    String orgId;

    // 机构名称
    String orgName;

    // 用于关联获取省份信息的机构(转运中心的id)
    @TransientSink
    String joinOrgId;

    // 城市 ID
    String cityId;

    // 城市名称
    String cityName;

    // 省份 ID
    String provinceId;

    // 省份名称
    String provinceName;

    // 分拣次数
    Long sortCountBase;

    // 时间戳
    Long ts;
}
