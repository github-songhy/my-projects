package com.realtime.tms.publisher.mapper;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @author Felix
 * @date 2025/3/3
 * 交易域统计mapper
 */
public interface TradeStatsMapper {
    //获取某天下单总金额
    @Select("select \n" +
        "\tsum(order_amount)\n" +
        "from\n" +
        "    (\n" +
        "\t\tselect \n" +
        "\t\t    cur_date,\n" +
        "\t\t    org_id,\n" +
        "\t\t    org_name,\n" +
        "\t\t    city_id,\n" +
        "\t\t    city_name,\n" +
        "\t\t    argMaxMerge(order_amount) as order_amount,\n" +
        "\t\t    argMaxMerge(order_count) as order_count \n" +
        "\t\tfrom dws_trade_org_order_day where toYYYYMMDD(cur_date)=#{date}\n" +
        "\t\tgroup by cur_date,\n" +
        "\t\t    org_id,\n" +
        "\t\t    org_name,\n" +
        "\t\t    city_id,\n" +
        "\t\t    city_name\n" +
        "    )")
    BigDecimal selectOrderAmount(Integer date);
}