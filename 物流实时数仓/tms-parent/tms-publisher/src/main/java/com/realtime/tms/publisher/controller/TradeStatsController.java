package com.realtime.tms.publisher.controller;

import com.realtime.tms.publisher.service.TradeStatsService;
import com.realtime.tms.publisher.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Felix
 * @date 2025/3/3
 * 交易域统计Controller
 */
@RestController
public class TradeStatsController {

    @Autowired
    private TradeStatsService tradeStatsService;

    @RequestMapping("/orderAmount")
    public String getOrderAmount(@RequestParam(value = "date",defaultValue = "0") Integer date){
        if(date == 0){
            date = DateFormatUtil.now();
        }
        BigDecimal orderAmount = tradeStatsService.getOrderAmount(date);
        String json = "{\"status\": 0,\"data\": "+orderAmount+"}";
        return json;
    }
}
