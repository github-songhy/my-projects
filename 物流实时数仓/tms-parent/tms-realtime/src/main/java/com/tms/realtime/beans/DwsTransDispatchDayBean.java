package com.tms.realtime.beans;

import lombok.Builder;
import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/2
 * 物流域发单统计实体类
 */
@Data
@Builder
public class DwsTransDispatchDayBean {
    // 统计日期
    String curDate;

    // 发单数
    Long dispatchOrderCountBase;

    // 时间戳
    Long ts;
}
