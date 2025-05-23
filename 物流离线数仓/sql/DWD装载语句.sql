-- 1. dwd_trade_order_detail_inc
-- 1.1 首日装载
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
      where dt = '2024-10-10'
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
      where dt = '2024-10-10'
        and after.is_deleted = '0') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string);

-- 1.2 每日装载
insert overwrite table tms.dwd_trade_order_detail_inc
    partition (dt = '2023-01-11')
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name   cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       order_time,
       order_no,
       status,
       dic_for_status.name       status_name,
       collect_type,
       dic_for_collect_type.name collect_type_name,
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
       ts
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             date_format(
                     from_utc_timestamp(
                                 to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                          substr(after.create_time, 12, 8))) * 1000,
                                 'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
             ts
      from ods_order_cargo_inc
      where dt = '2023-01-11'
        and op = 'c') cargo
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
      where dt = '2023-01-11'
        and op = 'c') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2023-01-11'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2023-01-11'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2023-01-11'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string);

-- 2. dwd_trade_pay_suc_detail_inc
-- 2.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trade_pay_suc_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       payment_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(payment_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) payment_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 2.2 每日装载
with pay_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 payment_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') payment_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60010'
                  and after.status = '60020'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
           and status = '60010'
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trade_pay_suc_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       pay_info.payment_time,
       order_info.order_no,
       pay_info.status,
       pay_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       pay_info.payment_type,
       pay_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       pay_info.ts
from pay_info
         join order_info
              on pay_info.id = order_info.order_id;

-- 3. dwd_trade_order_cancel_detail_inc
-- 3.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trade_order_cancel_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       cancel_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       date_format(cancel_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) cancel_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status = '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string);

-- 3.2 每日装载
with cancel_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 cancel_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') cancel_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and after.status = '60999'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trade_order_cancel_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       cancel_info.cancel_time,
       order_info.order_no,
       cancel_info.status,
       cancel_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       cancel_info.ts
from cancel_info
         join order_info
              on cancel_info.id = order_info.order_id;

-- 4. dwd_trans_receive_detail_inc
-- 4.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trans_receive_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       receive_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(receive_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) receive_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60020'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 4.2 每日装载
with receive_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 receive_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') receive_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60020'
                  and after.status = '60030'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
           and (status = '60010' or
                status = '60020')
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trans_receive_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       receive_info.receive_time,
       order_info.order_no,
       receive_info.status,
       receive_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       receive_info.payment_type,
       receive_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       receive_info.ts
from receive_info
         join order_info
              on receive_info.id = order_info.order_id;

-- 5. dwd_trans_dispatch_detail_inc
-- 5.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trans_dispatch_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       dispatch_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(dispatch_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) dispatch_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60020'
        and after.status <> '60030'
        and after.status <> '60040'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 5.2 每日装载
with dispatch_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 dispatch_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') dispatch_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60040'
                  and after.status = '60050'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
           and (status = '60010' or
                status = '60020' or
                status = '60030' or
                status = '60040')
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trans_dispatch_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       dispatch_info.dispatch_time,
       order_info.order_no,
       dispatch_info.status,
       dispatch_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       dispatch_info.payment_type,
       dispatch_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       dispatch_info.ts
from dispatch_info
         join order_info
              on dispatch_info.id = order_info.order_id;

-- 6. dwd_trans_bound_finish_detail_inc
-- 6.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trans_bound_finish_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       bound_finish_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(bound_finish_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) bound_finish_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60020'
        and after.status <> '60030'
        and after.status <> '60040'
        and after.status <> '60050'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 6.2 每日装载
with bound_finish_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 bound_finish_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') bound_finish_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60050'
                  and after.status = '60060'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
           and (status = '60010' or
                status = '60020' or
                status = '60030' or
                status = '60040' or
                status = '60050')
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trans_bound_finish_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       bound_finish_info.bound_finish_time,
       order_info.order_no,
       bound_finish_info.status,
       bound_finish_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       bound_finish_info.payment_type,
       bound_finish_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       bound_finish_info.ts
from bound_finish_info
         join order_info
              on bound_finish_info.id = order_info.order_id;

-- 7. dwd_trans_deliver_suc_detail_inc
-- 7.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trans_deliver_suc_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       deliver_suc_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(deliver_suc_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) deliver_suc_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60020'
        and after.status <> '60030'
        and after.status <> '60040'
        and after.status <> '60050'
        and after.status <> '60060'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 7.2 每日装载
with deliver_suc_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 deliver_suc_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') deliver_suc_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60060'
                  and after.status = '60070'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
           and (status = '60010' or
                status = '60020' or
                status = '60030' or
                status = '60040' or
                status = '60050' or
                status = '60060')
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trans_deliver_suc_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       deliver_suc_info.deliver_suc_time,
       order_info.order_no,
       deliver_suc_info.status,
       deliver_suc_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       deliver_suc_info.payment_type,
       deliver_suc_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       deliver_suc_info.ts
from deliver_suc_info
         join order_info
              on deliver_suc_info.id = order_info.order_id;

-- 8. dwd_trans_sign_detail_inc
-- 8.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trans_sign_detail_inc
    partition (dt)
select cargo.id,
       order_id,
       cargo_type,
       dic_for_cargo_type.name                 cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       sign_time,
       order_no,
       status,
       dic_for_status.name                     status_name,
       collect_type,
       dic_for_collect_type.name               collect_type_name,
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
       dic_for_payment_type.name               payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(sign_time, 'yyyy-MM-dd') dt
from (select after.id,
             after.order_id,
             after.cargo_type,
             after.volumn_length,
             after.volumn_width,
             after.volumn_height,
             after.weight,
             ts
      from ods_order_cargo_inc
      where dt = '2024-10-10'
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
             concat(substr(after.receiver_name, 1, 1), '*')                                  receiver_name,
             after.sender_complex_id,
             after.sender_province_id,
             after.sender_city_id,
             after.sender_district_id,
             concat(substr(after.sender_name, 1, 1), '*')                                    sender_name,
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                              estimate_arrive_time,
             after.distance,
             concat(substr(after.update_time, 1, 10), ' ', substr(after.update_time, 12, 8)) sign_time
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.status <> '60010'
        and after.status <> '60020'
        and after.status <> '60030'
        and after.status <> '60040'
        and after.status <> '60050'
        and after.status <> '60060'
        and after.status <> '60070'
        and after.status <> '60999') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 8.2 每日装载
with sign_info
         as
         (select without_status.id,
                 order_no,
                 status,
                 dic_for_status.name status_name,
                 collect_type,
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
                 dic_type_name.name  payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 sign_time,
                 ts
          from (select after.id,
                       after.order_no,
                       after.status,
                       after.collect_type,
                       after.user_id,
                       after.receiver_complex_id,
                       after.receiver_province_id,
                       after.receiver_city_id,
                       after.receiver_district_id,
                       concat(substr(after.receiver_name, 1, 1), '*')       receiver_name,
                       after.sender_complex_id,
                       after.sender_province_id,
                       after.sender_city_id,
                       after.sender_district_id,
                       concat(substr(after.sender_name, 1, 1), '*')         sender_name,
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')                   estimate_arrive_time,
                       after.distance,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.update_time, 1, 10), ' ',
                                                                    substr(after.update_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') sign_time,
                       ts
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'u'
                  and before.status = '60070'
                  and after.status = '60080'
                  and after.is_deleted = '0') without_status
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on without_status.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_type_name
               on without_status.payment_type = cast(dic_type_name.id as string)),
     order_info
         as (
         select id,
                order_id,
                cargo_type,
                cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                status_name,
                collect_type,
                collect_type_name,
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
                payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from dwd_trade_order_process_inc
         where dt = '9999-12-31'
         union
         select cargo.id,
                order_id,
                cargo_type,
                dic_for_cargo_type.name   cargo_type_name,
                volumn_length,
                volumn_width,
                volumn_height,
                weight,
                order_time,
                order_no,
                status,
                dic_for_status.name       status_name,
                collect_type,
                dic_for_collect_type.name collect_type_name,
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
                ''                        payment_type,
                ''                        payment_type_name,
                cargo_num,
                amount,
                estimate_arrive_time,
                distance
         from (select after.id,
                      after.order_id,
                      after.cargo_type,
                      after.volumn_length,
                      after.volumn_width,
                      after.volumn_height,
                      after.weight,
                      date_format(
                              from_utc_timestamp(
                                          to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                   substr(after.create_time, 12, 8))) * 1000,
                                          'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                      ts
               from ods_order_cargo_inc
               where dt = '2023-01-11'
                 and op = 'c') cargo
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
               where dt = '2023-01-11'
                 and op = 'c') info
              on cargo.order_id = info.id
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_cargo_type
              on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_status
              on info.status = cast(dic_for_status.id as string)
                  left join
              (select id,
                      name
               from ods_base_dic_full
               where dt = '2023-01-11'
                 and is_deleted = '0') dic_for_collect_type
              on info.collect_type = cast(dic_for_cargo_type.id as string))
insert overwrite table tms.dwd_trans_sign_detail_inc
    partition(dt = '2023-01-11')
select order_info.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       sign_info.sign_time,
       order_info.order_no,
       sign_info.status,
       sign_info.status_name,
       order_info.collect_type,
       collect_type_name,
       order_info.user_id,
       order_info.receiver_complex_id,
       order_info.receiver_province_id,
       order_info.receiver_city_id,
       order_info.receiver_district_id,
       order_info.receiver_name,
       order_info.sender_complex_id,
       order_info.sender_province_id,
       order_info.sender_city_id,
       order_info.sender_district_id,
       order_info.sender_name,
       sign_info.payment_type,
       sign_info.payment_type_name,
       order_info.cargo_num,
       order_info.amount,
       order_info.estimate_arrive_time,
       order_info.distance,
       sign_info.ts
from sign_info
         join order_info
              on sign_info.id = order_info.order_id;

-- 9. dwd_trade_order_process_inc
-- 9.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tms.dwd_trade_order_process_inc
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
       payment_type,
       dic_for_payment_type.name             payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       date_format(order_time, 'yyyy-MM-dd') start_date,
       end_date,
       end_date                              dt
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
      where dt = '2024-10-10'
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
             after.payment_type,
             after.cargo_num,
             after.amount,
             date_format(from_utc_timestamp(
                                 cast(after.estimate_arrive_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')             estimate_arrive_time,
             after.distance,
             if(after.status = '60080' or
                after.status = '60999',
                concat(substr(after.update_time, 1, 10)),
                '9999-12-31')                               end_date
      from ods_order_info_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0') info
     on cargo.order_id = info.id
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_cargo_type
     on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_status
     on info.status = cast(dic_for_status.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_collect_type
     on info.collect_type = cast(dic_for_cargo_type.id as string)
         left join
     (select id,
             name
      from ods_base_dic_full
      where dt = '2024-10-10'
        and is_deleted = '0') dic_for_payment_type
     on info.payment_type = cast(dic_for_payment_type.id as string);

-- 9.2 每日装载
with tmp
         as
         (select id,
                 order_id,
                 cargo_type,
                 cargo_type_name,
                 volumn_length,
                 volumn_width,
                 volumn_height,
                 weight,
                 order_time,
                 order_no,
                 status,
                 status_name,
                 collect_type,
                 collect_type_name,
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
                 payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 ts,
                 start_date,
                 end_date
          from dwd_trade_order_process_inc
          where dt = '9999-12-31'
          union
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
                 payment_type,
                 dic_for_payment_type.name             payment_type_name,
                 cargo_num,
                 amount,
                 estimate_arrive_time,
                 distance,
                 ts,
                 date_format(order_time, 'yyyy-MM-dd') start_date,
                 '9999-12-31'                          end_date
          from (select after.id,
                       after.order_id,
                       after.cargo_type,
                       after.volumn_length,
                       after.volumn_width,
                       after.volumn_height,
                       after.weight,
                       date_format(
                               from_utc_timestamp(
                                           to_unix_timestamp(concat(substr(after.create_time, 1, 10), ' ',
                                                                    substr(after.create_time, 12, 8))) * 1000,
                                           'GMT+8'), 'yyyy-MM-dd HH:mm:ss') order_time,
                       ts
                from ods_order_cargo_inc
                where dt = '2023-01-11'
                  and op = 'c') cargo
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
                       after.payment_type,
                       after.cargo_num,
                       after.amount,
                       date_format(from_utc_timestamp(
                                           cast(after.estimate_arrive_time as bigint), 'UTC'),
                                   'yyyy-MM-dd HH:mm:ss')             estimate_arrive_time,
                       after.distance
                from ods_order_info_inc
                where dt = '2023-01-11'
                  and op = 'c') info
               on cargo.order_id = info.id
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_cargo_type
               on cargo.cargo_type = cast(dic_for_cargo_type.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_status
               on info.status = cast(dic_for_status.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_collect_type
               on info.collect_type = cast(dic_for_cargo_type.id as string)
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_payment_type
               on info.payment_type = cast(dic_for_payment_type.id as string)),
     inc
         as
         (select without_type_name.id,
                 status,
                 payment_type,
                 dic_for_payment_type.name payment_type_name
          from (select id,
                       status,
                       payment_type
                from (select after.id,
                             after.status,
                             after.payment_type,
                             row_number() over (partition by after.id order by ts desc) rn
                      from ods_order_info_inc
                      where dt = '2023-01-11'
                        and op = 'u'
                        and after.is_deleted = '0'
                     ) inc_origin
                where rn = 1) without_type_name
                   left join
               (select id,
                       name
                from ods_base_dic_full
                where dt = '2023-01-11'
                  and is_deleted = '0') dic_for_payment_type
               on without_type_name.payment_type = cast(dic_for_payment_type.id as string)
         )
insert overwrite table dwd_trade_order_process_inc
    partition(dt)
select tmp.id,
       order_id,
       cargo_type,
       cargo_type_name,
       volumn_length,
       volumn_width,
       volumn_height,
       weight,
       order_time,
       order_no,
       inc.status,
       status_name,
       collect_type,
       collect_type_name,
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
       inc.payment_type,
       inc.payment_type_name,
       cargo_num,
       amount,
       estimate_arrive_time,
       distance,
       ts,
       start_date,
       if(inc.status = '60080' or
          inc.status = '60999',
          '2023-01-11', tmp.end_date) end_date,
       if(inc.status = '60080' or
          inc.status = '60999',
          '2023-01-11', tmp.end_date) dt
from tmp
         left join inc
                   on tmp.order_id = inc.id;

-- 10. dwd_trans_trans_finish_inc
-- 10.1 首日装载
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table dwd_trans_trans_finish_inc
    partition (dt)
select info.id,
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
       actual_start_time,
       actual_end_time,
       est_end_time estimate_end_time,
       actual_distance,
       finish_dur_sec,
       ts,
       dt
from (select after.id,
             after.shift_id,
             after.line_id,
             after.start_org_id,
             after.start_org_name,
             after.end_org_id,
             after.end_org_name,
             after.order_num,
             after.driver1_emp_id,
             concat(substr(after.driver1_name, 1, 1), '*')                                            driver1_name,
             after.driver2_emp_id,
             concat(substr(after.driver2_name, 1, 1), '*')                                            driver2_name,
             after.truck_id,
             md5(after.truck_no)                                                                      truck_no,
             date_format(from_utc_timestamp(
                                 cast(after.actual_start_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                                       actual_start_time,
             date_format(from_utc_timestamp(
                                 cast(after.actual_end_time as bigint), 'UTC'),
                         'yyyy-MM-dd HH:mm:ss')                                                       actual_end_time,

             after.actual_distance,
             (cast(after.actual_end_time as bigint) - cast(after.actual_start_time as bigint)) / 1000 finish_dur_sec,
             ts,
             date_format(from_utc_timestamp(
                                 cast(after.actual_end_time as bigint), 'UTC'),
                         'yyyy-MM-dd')                                                                dt
      from ods_transport_task_inc
      where dt = '2024-10-10'
        and after.is_deleted = '0'
        and after.actual_end_time is not null) info
         left join
     (select id,
             est_end_time
      from dim_shift_full
      where dt = '2024-10-10') dim_tb
     on info.shift_id = dim_tb.id;

-- 10.2 每日装载
insert overwrite table dwd_trans_trans_finish_inc
    partition (dt = '2023-01-11')
select info.id,
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
       actual_start_time,
       actual_end_time,
       est_end_time estimate_end_time,
       actual_distance,
       finish_dur_sec,
       ts
from (select after.id,
       after.shift_id,
       after.line_id,
       after.start_org_id,
       after.start_org_name,
       after.end_org_id,
       after.end_org_name,
       after.order_num,
       after.driver1_emp_id,
       concat(substr(after.driver1_name, 1, 1), '*')                                            driver1_name,
       after.driver2_emp_id,
       concat(substr(after.driver2_name, 1, 1), '*')                                            driver2_name,
       after.truck_id,
       md5(after.truck_no)                                                                      truck_no,
       date_format(from_utc_timestamp(
                           cast(after.actual_start_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss')                                                       actual_start_time,
       date_format(from_utc_timestamp(
                           cast(after.actual_end_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss')                                                       actual_end_time,
       after.actual_distance,
       (cast(after.actual_end_time as bigint) - cast(after.actual_start_time as bigint)) / 1000 finish_dur_sec,
       ts                                                                                       ts
from ods_transport_task_inc
where dt = '2023-01-11'
  and op = 'u'
  and before.actual_end_time is null
  and after.actual_end_time is not null
  and after.is_deleted = '0') info
         left join
     (select id,
             est_end_time
      from dim_shift_full
      where dt = '2024-10-10') dim_tb
     on info.shift_id = dim_tb.id;

-- 11. dwd_bound_inbound_inc
-- 11.1 首日装载
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
where dt = '2024-10-10';

-- 11.2 每日装载
insert overwrite table dwd_bound_inbound_inc
    partition (dt = '2023-01-11')
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.inbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') inbound_time,
       after.inbound_emp_id
from ods_order_org_bound_inc
where dt = '2023-01-11'
  and op = 'c';

-- 12. dwd_bound_sort_inc
-- 12.1 首日装载
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
                   'yyyy-MM-dd')          dt
from ods_order_org_bound_inc
where dt = '2024-10-10'
  and after.sort_time is not null;

-- 12.2 每日装载
insert overwrite table dwd_bound_sort_inc
    partition (dt = '2023-01-11')
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.sort_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') sort_time,
       after.sorter_emp_id
from ods_order_org_bound_inc
where dt = '2023-01-11'
  and op = 'u'
  and before.sort_time is null
  and after.sort_time is not null
  and after.is_deleted = '0';

-- 13. dwd_bound_outbound_inc
-- 13.1 首日装载
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
                   'yyyy-MM-dd')          dt
from ods_order_org_bound_inc
where dt = '2024-10-10'
  and after.outbound_time is not null;

-- 13.2 每日装载
insert overwrite table dwd_bound_outbound_inc
    partition (dt = '2023-01-11')
select after.id,
       after.order_id,
       after.org_id,
       date_format(from_utc_timestamp(
                           cast(after.outbound_time as bigint), 'UTC'),
                   'yyyy-MM-dd HH:mm:ss') outbound_time,
       after.outbound_emp_id
from ods_order_org_bound_inc
where dt = '2023-01-11'
  and op = 'u'
  and before.outbound_time is null
  and after.outbound_time is not null
  and after.is_deleted = '0';
