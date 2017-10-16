package cn.crxy.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.*;
import com.alibaba.fastjson.JSONObject;
/**
 * Created by zenith on 2016/2/23.
 */
public class RechargeDetailBolt extends BaseRichBolt {

	private OutputCollector _collector;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
		
	}

	@Override
	public void execute(Tuple tuple) {
		this._collector.ack(tuple);
		String log = tuple.getStringByField("str");
        //appid	param	time
        if (null != log && log.length() > 0) {
            String[] strs = log.split("\t");
            if (strs.length == 3) {
            	String param=strs[1];
                JSONObject jsonObject = JSONObject.parseObject(param);
                String ugctype= (String) jsonObject.get("ugctype");
                Integer appId=Integer.parseInt(strs[0]);
                if(ugctype!=null&&ugctype.equals("recharge")){
                    Integer userId=jsonObject.getInteger("userId");
                    Double rmb=jsonObject.getDouble("rmb");
                    Integer number=jsonObject.getInteger("number");
                	this._collector.emit(new Values(appId,userId,rmb,number));
                }
            }
        }
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("appId","userId","rmb","number"));
	}

}
