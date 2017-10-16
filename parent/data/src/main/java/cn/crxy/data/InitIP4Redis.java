package cn.crxy.data;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class InitIP4Redis {

	
	public static void main(String[] args) throws Exception {
		
		Jedis jedis = RedisUtils.getJedis();
//
//		BufferedReader reader=new BufferedReader(new FileReader(new File("D:\\视频\\第13期\\2016-08-20【互联网日志分析&多线程】\\互联网日志分析\\数据清洗\\code\\ip.data")));
//		String line=null;
//		String[] worlds=null;
//		while(null!=(line=reader.readLine())){
//			worlds=line.split("\t");
//			if(worlds.length==3){
//				//jedis.hset("ip:crxy",worlds[0],worlds[1]+"\t"+worlds[2]);
//				jedis.set("ip:"+worlds[0], worlds[1]+"\t"+worlds[2]);
//			}
//			
//		}
//		reader.close();
		
		//System.out.println(jedis.hget("ip:crxy","221.195.40.145"));
		System.out.println(jedis.get("ip:218.75.75.133"));
		
	}
}
