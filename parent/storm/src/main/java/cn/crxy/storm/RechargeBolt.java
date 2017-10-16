package cn.crxy.storm;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
/**
 * Created by zenith on 2016/2/23.
 */
public class RechargeBolt extends BaseRichBolt {
	
    //存储区分appid的用户Id和充值金额<appid,<userid,u_total>>
	private ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Double>> appIdUserMap=new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Double>>();
    private Connection connection;
    private String sql;
    private Logger logger;
    private DateFormat dateformat;

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Map<String, Object> map = new HashMap<>();
        map.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 20);
        //return super.getComponentConfiguration();
        return map;
    }

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        Properties prop = new Properties();
        try {
            prop.load(RechargeBolt.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(prop.getProperty("mysql.url")
                    , prop.getProperty("mysql.username"), prop.getProperty("mysql.password"));
            logger=Logger.getLogger(RechargeBolt.class);
            dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void execute(Tuple tuple) {

    	//该统计数据了
        if (tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)) {
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


        } else {
        	//组装数据
        	try{
	            //"appId","userId","rmb","number"
	            Integer appId=tuple.getIntegerByField("appId");
		        Integer userId=tuple.getIntegerByField("userId");
		        Double rmb=tuple.getDoubleByField("rmb");
		        //Integer number=tuple.getIntegerByField("number");
		        //分app维度
		        ConcurrentHashMap<Integer, Double> concurrentHashMap;
				if(!appIdUserMap.containsKey(appId)){
					concurrentHashMap=new ConcurrentHashMap<Integer, Double>();
					concurrentHashMap.put(userId, rmb);
				}else{
					concurrentHashMap = appIdUserMap.get(appId);
					if(concurrentHashMap.containsKey(userId)){
						concurrentHashMap.put(userId, concurrentHashMap.get(userId)+rmb);
					}else{
						concurrentHashMap.put(userId, rmb);
					}
				}
				appIdUserMap.put(appId, concurrentHashMap);
		            
		            
		            
	        	}catch(Exception ex){
	        		ex.printStackTrace();
	        		logger.error(ex.getMessage());
	        	}
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
    
}
