#!/bin/bash
#1、判断表名是否传入
if [ $ -lt 1 ]
then
	echo "必须传入all/表名..., 第二个参数为统计日期可选，默认为昨天"
	exit
fi

#2、判断日期是否传入,传入则记载指定日期数据,否则加载前一天数据
[ "$2" ] && datestr=$2 || datestr=$(date -d '-1 day' +%F)

dwd_trade_order_detail_inc_sql="
--采用动态分区初始加载和每日加载数据可以是同一个sql
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trade_order_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name               cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       order_time,
       order_no,
       status,
       dic_for_status.name                   status_name,
       collect_type,
       dic_for_collect_type.name             collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(order_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             concat(substr(after.create_time, 1, 10), ' ', substr(after.create_time, 12, 8)) order_time,
             ts
      from ods_order_cargo_inc
      where dt = '${datestr}'
        and after.is_deleted = '0') cargo
         join
     (select after.id,
             after.order_no,
             after.status,
             after.collect_type,
             after.user_id,
             after.receiver_complex_id,
             after.receiver_province_id,
             after.receiver_city_id,
             after.receiver_district_id,
             concat(substr(after.receiver_name, 1, 1), '*') receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')   sender_name,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')             estimate_arrive_time,
             after.distance
      from ods_order_info_inc
      where dt = '${datestr}'
        and after.is_deleted = '0') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '${datestr}'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '${datestr}'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '${datestr}'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string);
"

# 我写的，支付成功的放到相应的分区就行了，不需要关联之前未支付的数据，且统一首日和每日sql
dwd_trade_pay_suc_detail_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
--                 and after.status not in ('60010', '60999')
                   and (op = 'r' or after.status = '60020')),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trade_pay_suc_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       -- 首日加载无法获取到支付时间，只能以最终更新的时间作为支付时间
       update_time                             payment_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"


# 我写的，取消订单的数据直接放入对应分区就行了，依然是动态分区，首日和每日通用
dwd_trade_order_cancel_detail_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")          estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        after.amount,
                        ts,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8)) end_time
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
                   and after.status = "60999"),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert
overwrite
table
dwd_trade_order_cancel_detail_inc
partition
(
dt
)
select cast(huowu.id as bigint) id,
       order_id,
       cargo_type,
       d1.name                  cargo_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       end_time,
       order_no,
       status,
       "取消"                   status_name,
       collect_type,
       d2.name                  collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(end_time, 0, 10)  dt
from huowu
         join dingdan
              on cast(dingdan.id as bigint) = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(dingdan.collect_type as bigint) = d2.id;"


# 同下单成功表
dwd_trans_receive_detail_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
                   and after.status not in ('60010','60020','60999')
				   and (op = 'r' or after.status = '60030')),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trans_receive_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       -- 首日加载无法获取到揽收时间，只能以最终更新的时间作为揽收时间
       update_time                             receive_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"


# 同下单成功表
dwd_trans_dispatch_detail_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
				   and after.status not in ('60010','60020','60030', '60999')
				   and (op = 'r' or after.status = '60040')),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trans_dispatch_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       -- 首日加载无法获取到发单时间，只能以最终更新的时间作为发单时间
       update_time                             dispath_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"



# 同下单成功表
dwd_trans_bound_finish_detail_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
                   and after.status in ('60060','60070','60080')
				   and (op = 'r' or after.status = '60060')),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trans_bound_finish_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       -- 首日加载无法获取到转运完成时间，只能以最终更新的时间作为转运完成时间
       update_time                             bound_finish_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"



# 同下单成功表
dwd_trans_deliver_suc_detail_inc_sql="set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
--                    首日
                   and after.status in ('60070','60080')
				   and (op = 'r' or after.status = '60070')),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trans_deliver_suc_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       -- 首日加载无法获取到派送成功时间，只能以最终更新的时间作为派送成功时间
       update_time                             deliver_suc_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"



			  
# 同下单成功表 首日和每日可以是同一个sql
dwd_trans_sign_detail_inc_sql="			  
set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select after.id,
                        after.order_no,
                        after.status,
                        after.collect_type,
                        after.user_id,
                        after.receiver_complex_id,
                        after.receiver_province_id,
                        after.receiver_city_id,
                        after.receiver_district_id,
                        after.receiver_name,
                        after.sender_complex_id,
                        after.sender_province_id,
                        after.sender_city_id,
                        after.sender_district_id,
                        after.sender_name,
                        date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                    "yyyy-MM-dd HH:mm:ss")                                              estimate_arrive_time,
                        after.distance,
                        after.cargo_num,
                        concat(substring(after.create_time, 0, 10), " ",
                               substring(after.create_time, 12, 8))                                     order_time,
                        after.amount,
                        after.payment_type,
                        concat(substr(after.update_time, 0, 10), " ", substr(after.update_time, 12, 8)) update_time,
                        ts
                 from ods_order_info_inc
                 where dt = "$datestr"
                   and after.is_deleted = "0"
--                    首日和每日
                    and after.status = "60080"
                 ),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert overwrite table dwd_trans_sign_detail_inc partition (dt)
select cast(huowu.id as bigint),
       order_id,
       cargo_type,
       d1.name                                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       update_time                             sign_time,
       order_no,
       status,
       d2.name status_name,
       collect_type,
       d3.name                                 collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name                                 payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substr(update_time, 0, 10)              dt
from huowu
         join dingdan new on new.id = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id;"


# 我的写法
dwd_trade_order_process_inc_sql="set hive.exec.dynamic.partition.mode=nonstrict;
with dingdan as (select *
             from (select after.id,
                          after.order_no,
                          after.status,
                          after.collect_type,
                          after.user_id,
                          after.receiver_complex_id,
                          after.receiver_province_id,
                          after.receiver_city_id,
                          after.receiver_district_id,
                          after.receiver_name,
                          after.sender_complex_id,
                          after.sender_province_id,
                          after.sender_city_id,
                          after.sender_district_id,
                          after.sender_name,
                          date_format(cast(cast(after.estimate_arrive_time as bigint) as timestamp),
                                      "yyyy-MM-dd HH:mm:ss")                         estimate_arrive_time,
                          after.distance,
                          after.cargo_num,
                          concat(substring(after.create_time, 0, 10), " ",
                                 substring(after.create_time, 12, 8))                order_time,
                          after.amount,
                          after.payment_type,
                          after.update_time,
                          ts,
                          if(after.status in ("60080", "60999"), substr(after.update_time, 0, 10),
                             "9999-12-31")                                           end_time,
                          row_number() over (partition by after.id order by ts desc) rn
                   from ods_order_info_inc
                   where dt = "$datestr"
                     and after.is_deleted = "0") t1
             where rn = 1),
     huowu as (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight
               from ods_order_cargo_inc
                    -- 货物表不能查询当日的，因为之前的订单，其状态变化，货物信息不会变，还是创建订单时的信息
            -- where dt = "$datestr"
               where after.is_deleted = "0"),
     dict as (select id,
                     name
              from ods_base_dic_full
              where dt = "$datestr"
                and is_deleted = "0")
insert
overwrite
table
dwd_trade_order_process_inc
partition
(
dt
)
select cast(huowu.id as bigint)     id,
       order_id,
       cargo_type,
       d1.name                      cargo_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       order_time,
       order_no,
       status,
       d2.name                      status_name,
       collect_type,
       d3.name                      collect_type_name,
       user_id,
       receiver_complex_id,
       receiver_province_id,
       receiver_city_id,
       receiver_district_id,
       receiver_name,
       sender_complex_id,
       sender_province_id,
       sender_city_id,
       sender_district_id,
       sender_name,
       payment_type,
       d4.name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       substring(order_time, 0, 10) start_time,
       end_time,
       end_time                     dt
from huowu
         join dingdan new on cast(new.id as bigint) = huowu.order_id
         left join dict d1 on cast(huowu.cargo_type as bigint) = d1.id
         left join dict d2 on cast(new.status as bigint) = d2.id
         left join dict d3 on cast(new.collect_type as bigint) = d3.id
         left join dict d4 on cast(new.payment_type as bigint) = d4.id
union all
-- 当日未更新的状态订单直接放入9999-12-31
select *
from dwd_trade_order_process_inc
where dt = "9999-12-31"
  and id not in (select id from dingdan new);"

# 交易完成
# 首日和每日可以是同一个sql
dwd_trans_trans_finish_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
with t_task as (select after.id,
                       after.shift_id,
                       after.line_id,
                       after.start_org_id,
                       after.start_org_name,
                       after.end_org_id,
                       after.end_org_name,
                       after.order_num,
                       after.driver1_emp_id,
                       after.driver1_name,
                       after.driver2_emp_id,
                       after.driver2_name,
                       after.truck_id,
                       after.truck_no,
                       after.actual_start_time,
                       after.actual_end_time,
                       after.actual_distance,
                       after.create_time,
                       ts
                from ods_transport_task_inc
                where dt = "$datestr"
                  and after.is_deleted = "0"
                  and after.actual_end_time is not null),
     t_line as (select id,
                       estimated_time
                from ods_line_base_info_full
                where dt = "$datestr"
                  and is_deleted = "0")
insert overwrite table dwd_trans_trans_finish_inc partition (dt)
select t_task.id,
       shift_id,
       line_id,
       start_org_id,
       start_org_name,
       end_org_id,
       end_org_name,
       order_num,
       driver1_emp_id,
       driver1_name,
       driver2_emp_id,
       driver2_name,
       truck_id,
       truck_no,
       date_format(cast(cast(actual_start_time as bigint) as timestamp),
                   "yyyy-MM-dd HH:mm:ss")           actual_start_time,
       date_format(cast(cast(actual_end_time as bigint) as timestamp),
                   "yyyy-MM-dd HH:mm:ss")           actual_end_time,
       date_format(cast(cast((estimated_time * 60 * 1000 + actual_start_time) as bigint) as timestamp),
                   "yyyy-MM-dd HH:mm:ss")           estimate_end_time,
       actual_distance,
       (actual_end_time - actual_start_time) / 1000 finish_dur_sec,
       ts,
       date_format(cast(cast(actual_end_time as bigint) as timestamp),
                   "yyyy-MM-dd")                    dt
from t_task
         join t_line on t_task.line_id = t_line.id;"



dwd_bound_inbound_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_bound_inbound_inc
    partition (dt)
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.inbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') inbound_time,
       after.inbound_emp_id,
       date_format(from_utc_timestamp(
                           cast(after.inbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd')          dt
from ods_order_org_bound_inc
where dt = '$datestr'
  and after.is_deleted = '0'
--op = r 就是首日，op = c 就是每日新入库的
  and (op in ('r', 'c'));
"

dwd_bound_sort_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_bound_sort_inc
    partition (dt)
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.sort_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') sort_time,
       after.sorter_emp_id,
       date_format(from_utc_timestamp(
                           cast(after.sort_time as bigint), 'UTC'),
                   'yyyy-MM-dd') dt
from ods_order_org_bound_inc
where dt = '$datestr'
  and after.is_deleted = '0'
  -- 必须条件
  and after.sort_time is not null
  -- 首日数据的op = r，每日的数据新增 op != r 但只要before.sort_time is null就是刚分拣的 用stauts判断也行
  and (op = 'r' or before.sort_time is null);
"

dwd_bound_outbound_inc_sql="
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_bound_outbound_inc
    partition (dt)
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.outbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') outbound_time,
       after.outbound_emp_id,
       date_format(from_utc_timestamp(
                           cast(after.outbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd') dt
from ods_order_org_bound_inc
where dt = '$datestr'
  and after.is_deleted = '0'
  -- 必须条件
  and after.outbound_time is not null
  -- 首日数据的op = r，每日的数据新增 op != r 但只要before.outbound_time is null就是刚出库的 用stauts判断也行
  and (op = 'r' or before.outbound_time is null);
"
#3、根据表名匹配加载数据
case $1 in
"all")
	/export/server/hive/bin/hive -e "use tms;${dwd_bound_inbound_inc_sql}${dwd_bound_outbound_inc_sql}${dwd_bound_sort_inc_sql}${dwd_trade_order_cancel_detail_inc_sql}${dwd_trade_order_detail_inc_sql}${dwd_trade_pay_suc_detail_inc_sql}${dwd_trans_bound_finish_detail_inc_sql}${dwd_trans_deliver_suc_detail_inc_sql}${dwd_trans_dispatch_detail_inc_sql}${dwd_trans_receive_detail_inc_sql}${dwd_trans_sign_detail_inc_sql}${dwd_trans_trans_finish_inc_sql}${dwd_trade_order_process_inc_sql}"
;;
"dwd_bound_inbound_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_bound_inbound_inc_sql}"
;;
"dwd_bound_outbound_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_bound_outbound_inc_sql}"
;;
"dwd_bound_sort_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_bound_sort_inc_sql}"
;;
"dwd_trade_order_cancel_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trade_order_cancel_detail_inc_sql}"
;;
"dwd_trade_order_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trade_order_detail_inc_sql}"
;;
"dwd_trade_order_process_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trade_order_process_inc_sql}"
;;
"dwd_trade_pay_suc_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trade_pay_suc_detail_inc_sql}"
;;
"dwd_trans_bound_finish_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_bound_finish_detail_inc_sql}"
;;
"dwd_trans_deliver_suc_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_deliver_suc_detail_inc_sql}"
;;
"dwd_trans_dispatch_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_dispatch_detail_inc_sql}"
;;
"dwd_trans_receive_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_receive_detail_inc_sql}"
;;
"dwd_trans_sign_detail_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_sign_detail_inc_sql}"
;;
"dwd_trans_trans_finish_inc")
    /export/server/hive/bin/hive -e "use tms;${dwd_trans_trans_finish_inc_sql}"
;;
*)
	echo "表名输入错误..."
;;
esac
