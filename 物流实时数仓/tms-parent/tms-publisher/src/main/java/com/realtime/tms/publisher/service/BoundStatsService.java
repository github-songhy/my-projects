package com.realtime.tms.publisher.service;

import com.realtime.tms.publisher.beans.BoundSortBean;

import java.util.List;

/**
 * @author Felix
 * @date 2025/3/3
 * 中转域统计service接口
 */
public interface BoundStatsService {
    List<BoundSortBean> getProvinceSort(Integer date);
}
