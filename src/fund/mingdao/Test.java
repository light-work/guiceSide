package fund.mingdao;

import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.client.producer.SendResult;
import com.taobao.metamorphosis.utils.ZkUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhenjiaWang on 14/12/9.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final MetaClientConfig metaClientConfig = new MetaClientConfig();
        final ZkUtils.ZKConfig zkConfig = new ZkUtils.ZKConfig();
        //设置zookeeper地址
        zkConfig.zkConnect = "114.251.98.92:2181";
        metaClientConfig.setZkConfig(zkConfig);
        // New session factory,强烈建议使用单例
        MessageSessionFactory sessionFactory = new MetaMessageSessionFactory(metaClientConfig);
        // create producer,强烈建议使用单例
        MessageProducer producer = sessionFactory.createProducer();
        // publish topic
        final String topic = "la_md_sys";
        producer.publish(topic);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        int index=1;
        System.out.println("type key words");
        while ((line = reader.readLine()) != null) {
            // send message
            SendResult sendResult = producer.sendMessage(new Message(topic, line.getBytes()));
            // check result
            if (!sendResult.isSuccess()) {
                System.err.println("Send message failed,error message:" + sendResult.getErrorMessage());
            }
            else {
                System.out.println("Send message successfully,sent to " + sendResult.getPartition());
            }
            index++;
            if(index==4){
                producer.shutdown();
                sessionFactory.shutdown();
                System.exit(0);
            }
        }
    }
}
