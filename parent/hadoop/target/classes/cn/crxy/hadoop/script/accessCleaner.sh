#!/bin/sh 
#引入环境变量
source /etc/profile
#定义一个遍历起始日期的变量 可以正常跑昨天一天的数据或者跑一段时间的数据
j=1
#定义一个终结日期变量
k=0

######windows 下的脚本文件ff为doc在unix执行会有问题。
#查看文件格式 (:set ff)
#设置格式 方式：  (:set ff=unix)

#j k 来控制指定的日期 时间段
while [ $j -gt $k ]
do
echo $j
dirYesterday=`date -d" $j days ago " +"%Y%m%d"`
yesterday=`date -d" $j days ago " +"%Y-%m-%d"`

let j=j-1
#把let j=j-1方最后会影响执行的结果状态码 如果放最后就是 执行结果为1

echo $dirYesterday
echo $yesterday

cd /opt/work/

#web data
#delete old data
path="/hive/warehouse/fruit.db/t_origin_access_stat_tbl/stat_date=$yesterday/"
hdfs dfs -test -e ${path}
if [ $? -eq 0 ];then 
hdfs dfs -rm -r ${path}
fi
#analysis log
hadoop jar /opt/work/jar/hadoop-0.0.1-SNAPSHOT-jar-with-dependencies.jar cn.crxy.hadoop.mr.AccessLogCleaner /source/access/$dirYesterday/*  /hive/warehouse/fruit.db/t_origin_access_stat_tbl/stat_date=$yesterday/
hive -e "LOAD DATA INPATH '/hive/warehouse/fruit.db/t_origin_access_stat_tbl/stat_date=$yesterday/part-r-*' INTO TABLE fruit.t_origin_access_stat_tbl PARTITION (stat_date='$yesterday')" 
done