package fund.mingdao;

import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.consumer.ConsumerConfig;
import com.taobao.metamorphosis.client.consumer.MessageConsumer;
import com.taobao.metamorphosis.client.consumer.MessageListener;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.client.producer.SendResult;
import com.taobao.metamorphosis.utils.ZkUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

/**
 * Created by zhenjiaWang on 14/12/9.
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        final MetaClientConfig metaClientConfig = new MetaClientConfig();
        final ZkUtils.ZKConfig zkConfig = new ZkUtils.ZKConfig();
        //设置zookeeper地址
        zkConfig.zkConnect = "114.251.98.92:2181";
        metaClientConfig.setZkConfig(zkConfig);
        // New session factory,强烈建议使用单例
        MessageSessionFactory sessionFactory = new MetaMessageSessionFactory(metaClientConfig);
        // subscribed topic
        final String topic = "la_md_sys";
        // consumer group
        final String group = "meta-example";
        // create consumer,强烈建议使用单例
        MessageConsumer consumer = sessionFactory.createConsumer(new ConsumerConfig(group));
        // subscribe topic
        consumer.subscribe(topic, 1024 * 1024, new MessageListener() {

            public void recieveMessages(Message message) {
                System.out.println("Receive message " + new String(message.getData()));
            }


            public Executor getExecutor() {
                // Thread pool to process messages,maybe null.
                return null;
            }
        });
        // complete subscribe
        consumer.completeSubscribe();
    }
}
