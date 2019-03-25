package cn.tedu.rabbitmq.home;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.util.Map;

/**
 * @Author chao
 * @Date 2019/3/19 - 11:12
 */
public class Test_1_simple_provider {
    
    @Test
    public void provider() throws Exception {
        // 1. 建立连接
        // 1.1 得到factory
        ConnectionFactory factory = new ConnectionFactory();
        // 1.2 设置服务器ip
        factory.setHost("192.168.220.139");
        // 1.3 设置端口号
        // rabbitmq服务器有两个端口号 
        // 15672/5672  返回页面/发消息收消息
        factory.setPort(5672);
        // 1.4 设置虚拟机主机
        factory.setVirtualHost("/pd");
        // 1.5 设置用户名和密码
        factory.setUsername("pdadmin");
        factory.setPassword("pdadmin");
        // 利用工厂对象新建连接
        Connection connection = factory.newConnection();


        // 2. 利用连接对象创建channel (IO流)
        Channel channel = connection.createChannel();

        // 3. 定义队列 包是rabbitmq.client中的
        String queueName = "orderQueue3";
        //false:队列不保存到硬盘
        boolean durable = true;
        //true:别的程序不能访问这个队列
        //false:别的程序能访问这个队列
        boolean exclusive = false;
        //不要删除这个队列
        boolean autoDelete = false;
        Map<String, Object> arguments = null;

        // 定义队列
        channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
        // 4. 发消息
        //交换机名称 ，值为"",使用的是默认交换机
        //测试10秒钟能发送多少数据
        String exchange = "";
        String routingKey = queueName;

        BasicProperties.Builder builder = new BasicProperties.Builder();
        builder.deliveryMode(2);  // 2 表示数据保存到硬盘
        BasicProperties props = builder.build();
        byte[] body = "order2".getBytes();

        // 发消息
        channel.basicPublish(exchange, routingKey, props, body);

        // 5. 关闭连接
        channel.close();
        connection.close();
    }
}
