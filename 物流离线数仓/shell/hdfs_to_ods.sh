#!/bin/bash
#hdfs_to_ods.sh all/表名 [日期]
#1、判断参数是否传入
if [ $# -lt 1 ]
then
	echo "必须传入all/表名..."
	exit
fi
#2、判断日期是否传入,传入则加载指定日期的数据,没传则加载前一天日期数据
[ "$2" ] && datestr=$2 || datestr=$(date -d '-1 day' +%F)


function load_data(){
	tables=$*
	sql="use tms;"
	for table in ${tables}
	do
		# 判断是否有对应日期的数据
		hdfs dfs -test -e /tms_origin_data/${table:4}/${datestr}
		# $?表示取上一条命令执行的结果,为0表示路径存在
		if [ $? -eq 0 ]
		then
			# ${table：4}表示从table的下标为4的字符开始截取
			sql="${sql};load data inpath '/tms_origin_data/${table:4}/${datestr}' overwrite into table ${table} partition (dt='${datestr}');"
		fi
	done
	hdfs dfs -test -e /tms_origin_data_bak
	if [ $? -eq 1 ]
	then
		hdfs dfs -mkdir /tms_origin_data_bak
	fi 
	hdfs dfs -cp /tms_origin_data /tms_origin_data_bak
	/export/server/hive/bin/hive -e "${sql}"
}

#3、根据第一个参数加载数据
case $1 in
"all")
	load_data "ods_base_complex_full" "ods_base_dic_full" "ods_base_organ_full" "ods_base_region_info_full" "ods_employee_info_full" "ods_express_courier_complex_full" "ods_express_courier_full" "ods_line_base_info_full" "ods_line_base_shift_full" "ods_order_cargo_inc" "ods_order_info_inc" "ods_order_org_bound_inc" "ods_transport_task_inc" "ods_truck_driver_full" "ods_truck_info_full" "ods_truck_model_full" "ods_truck_team_full" "ods_user_address_inc" "ods_user_info_inc"
;;
"ods_base_complex_full")
    load_data "ods_base_complex_full"
;;
"ods_base_dic_full")
    load_data "ods_base_dic_full"
;;
"ods_base_organ_full")
    load_data "ods_base_organ_full"
;;
"ods_base_region_info_full")
    load_data "ods_base_region_info_full"
;;
"ods_employee_info_full")
    load_data "ods_employee_info_full"
;;
"ods_express_courier_complex_full")
    load_data "ods_express_courier_complex_full"
;;
"ods_express_courier_full")
    load_data "ods_express_courier_full"
;;
"ods_line_base_info_full")
    load_data "ods_line_base_info_full"
;;
"ods_line_base_shift_full")
    load_data "ods_line_base_shift_full"
;;
"ods_order_cargo_inc")
    load_data "ods_order_cargo_inc"
;;
"ods_order_info_inc")
    load_data "ods_order_info_inc"
;;
"ods_order_org_bound_inc")
    load_data "ods_order_org_bound_inc"
;;
"ods_transport_task_inc")
    load_data "ods_transport_task_inc"
;;
"ods_truck_driver_full")
    load_data "ods_truck_driver_full"
;;
"ods_truck_info_full")
    load_data "ods_truck_info_full"
;;
"ods_truck_model_full")
    load_data "ods_truck_model_full"
;;
"ods_truck_team_full")
    load_data "ods_truck_team_full"
;;
"ods_user_address_inc")
    load_data "ods_user_address_inc"
;;
"ods_user_info_inc")
    load_data "ods_user_info_inc"
;;
*)
	echo "表名输入错误..."
;;
esac
