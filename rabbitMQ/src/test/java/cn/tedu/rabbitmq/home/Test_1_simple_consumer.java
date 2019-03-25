package cn.tedu.rabbitmq.home;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Test;

import java.util.HashMap;

/**
 * @Author chao
 * @Date 2019/3/19 - 14:08
 */
public class Test_1_simple_consumer {


    @Test
    public void consumer() throws Exception {
        // 1. 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.220.139");
        factory.setPort(5672);
        factory.setVirtualHost("/pd");
        factory.setUsername("pdadmin");
        factory.setPassword("pdadmin");
        Connection connection = factory.newConnection();

        // 2. 得到通道
        Channel channel = connection.createChannel();

        // 3. 定义队列
        String queueName = "orderQueue3";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        HashMap<String, Object> arguments = null;
        channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);

        // 4. 读消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        System.out.println("consumer 01 start ~");
        // 5. 遍历
        // 关闭的时候,把isRunning 赋值成false
        boolean isRunning = true;
        while (isRunning) {
            // delivery 封装了消息和消息id
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            // 取最后一个消息
            byte[] body = delivery.getBody();
            String mStr = new String(body);
            System.out.println("body = " + mStr);
        }
        channel.close();
        connection.close();
    }
}
