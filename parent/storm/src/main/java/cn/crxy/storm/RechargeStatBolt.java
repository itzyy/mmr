package cn.crxy.storm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class RechargeStatBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private String log;
	private String[] logs;
	private Integer appId=0;
	private String param;
	private Logger logger;
	private Integer userId=0;
	private Double rmb=0.0;
	private Connection connection;
	private DateFormat dateformat;
	
	//<appid ,<userid,u_total>>
	private ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Double>> appIdUserMap=new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Double>>();
	
	@Override
	public Map<String, Object> getComponentConfiguration() {
		
		Map<String,Object> map=new HashMap<String, Object>();
		
		map.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
		
		return map;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {

		Properties prop = new Properties();
        try {
            prop.load(RechargeStatBolt.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(prop.getProperty("mysql.url")
                    , prop.getProperty("mysql.username"), prop.getProperty("mysql.password"));
            logger=Logger.getLogger(RechargeStatBolt.class);
            dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
		_collector=collector;
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		//appid	param	time
		//{\"ugctype\":\"recharge\",\"userId\":\"20202\",\"rmb\":\"50\",\"number\":\"10\"}
		
		//统计的逻辑
		if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)){
			
			
			try {
				 String statTime=dateformat.format(new Date());
				//appid num rmb
				for (Integer appId : appIdUserMap.keySet()) {
					Integer num=appIdUserMap.get(appId).size();
					Double total=0.0;
					for (Double rmb : appIdUserMap.get(appId).values()) {
						total=total+rmb;
					}
					String sql="insert into t_view_app_recharge_online_tbl (appid,user_num,rmb,stat_time) values(?,?,?,?);";
					PreparedStatement ps = connection.prepareStatement(sql);
					ps = connection.prepareStatement(sql);
					ps.setInt(1, appId);
					ps.setInt(2, num);
					ps.setDouble(3, total);
					ps.setString(4, statTime);
					ps.executeUpdate();
					ps.close();
				}
				appIdUserMap.clear();
				
			
			
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			
			
			
			
		}else{
			//应答机制
			_collector.ack(input);
			//组装数据
			//appid	param	time
			//{\"ugctype\":\"recharge\",\"userId\":\"20202\",\"rmb\":\"50\",\"number\":\"10\"}
			log=input.getStringByField("str");
			logs=log.split("\t");
			if(logs.length==3){
				param=logs[1];
				if(param!=null){
					JSONObject json=JSONObject.parseObject(param);
					if(null!=json&&json.getString("ugctype").equals("recharge")){
						try {
							userId=json.getInteger("userId");
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						try {
							rmb=json.getDouble("rmb");
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						try {
							appId=Integer.parseInt(logs[0]);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						
						ConcurrentHashMap<Integer, Double> concurrentHashMap;
						if(!appIdUserMap.containsKey(appId)){
							concurrentHashMap=new ConcurrentHashMap<Integer, Double>();
							concurrentHashMap.put(userId, rmb);
						}else{
							concurrentHashMap = appIdUserMap.get(appId);
							if(concurrentHashMap.containsKey(userId)){
								//用户多次充值
								concurrentHashMap.put(userId, concurrentHashMap.get(userId)+rmb);
							}else{
								//该用户第一次充值
								concurrentHashMap.put(userId, rmb);
							}
						}
						appIdUserMap.put(appId, concurrentHashMap);
					}
					
					
					
				}
				
				
				
				
				
			}
			
			
		}
		
		
		
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}
    
    
}
