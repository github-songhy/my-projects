package com.tms.realtime.beans;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Felix
 * @date 2025/3/2
 * 物流域机构卡车类别粒度统计实体类
 */
@Data
@Builder
public class DwsTransOrgTruckModelTransFinishDayBean {
    // 统计日期
    String curDate;

    // 机构ID
    String orgId;

    // 机构名称
    String orgName;

    // 卡车ID
    @TransientSink
    String truckId;

    // 卡车型号ID
    String truckModelId;

    // 卡车型号名称
    String truckModelName;

    // 用于关联城市信息的一级机构ID
    @TransientSink
    String joinOrgId;

    // 城市ID
    String cityId;

    // 城市名称
    String cityName;

    // 运输完成次数
    Long transFinishCountBase;

    // 运输完成里程
    BigDecimal transFinishDistanceBase;

    // 运输完成历经时长
    Long transFinishDurTimeBase;

    // 时间戳
    Long ts;
}
