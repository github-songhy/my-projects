package com.realtime.tms.publisher.beans;

import lombok.Data;

/**
 * @author Felix
 * @date 2025/3/3
 */
@Data
public class BoundSortBean {
    String org_name;
    String city_name;
    String province_name;
    Long sort_count;
}
