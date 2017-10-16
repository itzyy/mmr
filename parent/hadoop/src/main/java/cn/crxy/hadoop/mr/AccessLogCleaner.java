package cn.crxy.hadoop.mr;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import cn.crxy.hadoop.utils.RedisUtils;
import cn.crxy.hadoop.utils.UserAgent;
import cn.crxy.hadoop.utils.UserAgentUtil;
import redis.clients.jedis.Jedis;

public class AccessLogCleaner {

	private  static Logger logger=Logger.getLogger(AccessLogCleaner.class);
	
	public static void main(String[] args) throws Exception {
		if(args.length<2){
			logger.error(" args need at least 2");
			System.exit(2);
		}
		Configuration configuration=new Configuration();
		Job job=Job.getInstance(configuration, "accessJob");
		job.setJarByClass(AccessLogCleaner.class);
		job.setMapperClass(AccessLogMapper.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		
		//输入
		for (int i = 0; i < args.length-1; i++) {
			FileInputFormat.addInputPath(job, new Path(args[i]));
		}
		//输出
		FileOutputFormat.setOutputPath(job, new Path(args[args.length-1]));
		//根据job的执行情况 结束程序  状态码 为0或者1
		System.exit(job.waitForCompletion(true)?0:1);
	}

	public static class AccessLogMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
		
		//appid	ip	mid	userid	login_type	request		status	http_referer	user_agent	time
		
		private String line="";
		private String[] strs=null;
		private Integer appid=0;
		private Integer userid=0;
		private Integer loginType=0;
		private Long time=0l;
		private String ipInfo="";
		
		private Logger logger;
		private Jedis jedis;
		private UserAgent userAgent;
		private DateFormat format;
		private Text result=new Text();
		
		@Override
		protected void setup(
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			logger=Logger.getLogger(AccessLogMapper.class);
			jedis=RedisUtils.getJedis();
			format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
		@Override
		protected void cleanup(
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			logger=null;
			jedis.close();
			format=null;
		}
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			if(null!=value&&value.toString().length()>0){
				 line=value.toString();
				 strs=line.split("\t");
				//过滤日志
				 if(strs.length==10){
					 
					 
					 try {
						 appid=Integer.parseInt(strs[0]);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					 try {
						 userid=Integer.parseInt(strs[3]);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					 try {
						 loginType=Integer.parseInt(strs[4]);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					 try {
						 time=Long.parseLong(strs[9]);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					 
					 
					 
					 
					 AccessLog log=new AccessLog(appid, strs[1], strs[2], userid, loginType, strs[5], strs[6], strs[7], strs[8], time);
					 
					 //解析字段
					 //解析ip 去redis去读
					 ipInfo=jedis.get("ip:"+log.getIp());
					 if(ipInfo!=null&&ipInfo.split("\t").length==2){
						 log.setProvince(ipInfo.split("\t")[0]);
						 log.setCity(ipInfo.split("\t")[1]);
					 }
					 //request：method path http_version。
					 if(log.getRequest()!=null&&log.getRequest().split(" ").length==3){
						 log.setMethod(log.getRequest().split(" ")[0]);
						 log.setPath(log.getRequest().split(" ")[1]);
							 log.setHttp_version(log.getRequest().split(" ")[2]);
					 }
					 //user agent  浏览器类型
					 userAgent = UserAgentUtil.getUserAgent(log.getUserAgent());
					 if(userAgent!=null){
						 log.setIeType(userAgent.getBrowserType());
					 }
					 //time
					 log.setDateTime(format.format(new Date(log.getTime())));
					 result.set(log.toString());
					 context.write(NullWritable.get(), result);
					 
				 }
			}
			
			
			
		}
		
		
		

		
	}
	
}
