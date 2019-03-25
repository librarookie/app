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
public class Test_1_simple_consumerACK {


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

        // 2. 新建通道
        Channel channel = connection.createChannel();

        // 3. 定义队列
        HashMap<String, Object> arguments = null;
        channel.queueDeclare("orderQueue3", true, false, false, arguments);

        // 4. 读消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // autoAck=false
        // 没有自动确认, 收到消息后, 服务器不会删除数据
        // 程序员手动发确认消息后
        channel.basicConsume("orderQueue3", false, consumer);
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
            // 数据处理正常, 手动发确认
            // 如果前面数据处理的代码发生异常, 不发确认
            // 数据还在拂去其上
            // 得到消息id
            long tag = delivery.getEnvelope().getDeliveryTag();
            channel.basicAck(tag, true);
        }
        channel.close();
        connection.close();
    }
}
