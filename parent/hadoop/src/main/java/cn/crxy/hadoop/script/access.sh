#!/usr/bin/env bash
source /etc/profile
stat_date=`date -d "yesterday" +"%Y-%m-%d"`

echo "job begin "
echo `date +"%Y-%m-%d %H:%M:%S"`
cd /opt/work

echo "begin accessCleaner"
sh /opt/work/shell/accessCleaner.sh > /opt/work/logs/accessCleaner.log.${stat_date} 2>&1


#login
{
echo "begin app_login_stat sh"
sh /opt/work/hive_script/app_login_stat.sh > /opt/work/logs/app_login_stat.log.${stat_date} 2>&1
}&
login_pid=$!
echo $login_pid

#uv pv 
{
echo "begin app_uv_pv_stat sh"
sh /opt/work/hive_script/app_uv_pv_stat.sh > /opt/work/logs/app_uv_pv_stat.log.${stat_date} 2>&1
}&
uv_pv_pid=$!
echo $uv_pv_pid

#one remain
{
echo "begin app_one_remain_stat sh"
sh /opt/work/hive_script/app_one_remain_stat.sh > /opt/work/logs/app_one_remain_stat.log.${stat_date} 2>&1
}&
one_remain_pid=$!
echo $one_remain_pid

wait $login_pid
wait $uv_pv_pid
wait $one_remain_pid

echo "begin kettle view"
sh /opt/data-integration/kitchen.sh -rep file_rep -job J_O_app_core_view_stat -dir /access/core_view -param:stat_date=${stat_date} -level Detailed > /opt/work/kettle_logs/J_O_app_core_view_stat.log.${stat_date} 2>&1
echo "end kettle view"

echo `date +"%Y-%m-%d %H:%M:%S"`
echo "job end "