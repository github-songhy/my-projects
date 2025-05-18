package com.realtime.tms.publisher.mapper;

import com.realtime.tms.publisher.beans.BoundSortBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Felix
 * @date 2025/3/3
 * 中转域统计mapper接口
 */
public interface BoundStatsMapper {
    //获取某天各个省份分拣数
    @Select("select\n" +
        "\tprovince_name,\n" +
        "\tsum(sort_count) sort_count\n" +
        "from(\n" +
        "\tSELECT\n" +
        "\t    cur_date,\n" +
        "\t    org_id,\n" +
        "\t    org_name,\n" +
        "\t    city_id,\n" +
        "\t    city_name,\n" +
        "\t    province_id,\n" +
        "\t    province_name,\n" +
        "\t    argMaxMerge(sort_count) AS sort_count\n" +
        "\tFROM dws_bound_org_sort_day where toYYYYMMDD(cur_date)=#{date}\n" +
        "\tGROUP BY\n" +
        "\t    cur_date,\n" +
        "\t    org_id,\n" +
        "\t    org_name,\n" +
        "\t    city_id,\n" +
        "\t    city_name,\n" +
        "\t    province_id,\n" +
        "\t    province_name\n" +
        ") group by province_name")
    List<BoundSortBean> selectProvinceSort(Integer date);
}
