package cn.crxy.storm;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
/**
 * Created by zenith on 2016/2/23.
 */
public class RechargeStatTopology {

    public static void main(String[] args) throws Exception {

       /* topic名字
        当前spout的唯一标识Id （以下代称$spout_id）
        zookeeper上用于存储当前处理到哪个Offset了 （以下代称$zk_root)
        当前topic中数据如何解码

        后面两个的目的，其实是在zookeeper上建立一个 $zk_root/$spout_id 的节点，其值是一个map，存放了当前Spout处理的Offset的信息。
        */

        //这个地方其实就是kafka配置文件里边的zookeeper.connect这个参数
        String brokerZkStr = "crxy155.crxy:2181,crxy156.crxy:2181,crxy162.crxy:2181";
        ZkHosts zkHosts = new ZkHosts(brokerZkStr);
        String topic = "rechargeStat";
        //汇报offset信息的root路径
        String offsetZkRoot = "/rechargeStatcluster";
        //存储该spoutid的消费offset信息,譬如以topoName来命名
        String offsetZkId  = "rechargeStatcluster";


        SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, topic, offsetZkRoot, offsetZkId);
        //就是告诉KafkaSpout如何去解码数据，生成Storm内部传递数据
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());


        KafkaSpout spout = new KafkaSpout(kafkaConfig);

        TopologyBuilder builder = new TopologyBuilder();
        //设置消费者的数目 此处分区是4所以适合4个并行度
        builder.setSpout("spout", spout, 4);//set parallelism hint to 4
        //builder.setBolt("bolt", new RechargeStatBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("bolt", new RechargeDetailBolt(),3).shuffleGrouping("spout");
        //按照统计的维度进行分组。前提是string的log转成字段组new Fields("appId","userId","rmb","number")
        builder.setBolt("statbolt", new RechargeBolt(),4).fieldsGrouping("bolt", new Fields("appId"));
        Config config = new Config();
        //这一行代码表示让spout每10秒发送一个特殊的tuple
        //config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
     
        config.setDebug(true);


        //提交远程
        if (args != null && args.length > 0) {
        	config.setNumWorkers(3); // use three worker processes
          StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
        else {
        	//提交本地模式
    	   LocalCluster cluster = new LocalCluster();
           cluster.submitTopology("rechargeStatTopology", config, builder.createTopology());
         // Thread.sleep(10000);
          //cluster.shutdown();
        }
    }

}