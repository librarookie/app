package com.tedu.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import org.junit.Test;

public class Test_4_direct_consumer2 {
	// 从消息队列服务器取消息
	@Test
	public void consumer() throws Exception {
		// 1.建立连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.200.139");
		factory.setPort(5672);
		factory.setUsername("pdadmin");
		factory.setPassword("pdadmin");
		factory.setVirtualHost("/pd");
		// 2,得到channel
		// com.rabbitmq.client
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		String exchangeName = "E2";
		// 创建交换机
		// fanout 订阅模式
		// direct 路由模式
		// topic 主题模式
		channel.exchangeDeclare(exchangeName, "direct");

		String queueName = "E2 queue2";
		// p2:durable true 持久化，保存到硬盘
		boolean durable = true;
		// p3:exclusive false 别的程序也能访问
		boolean exclusive = false;
		// p4:autoDelete false 队列中的消息处理完了，不删队列
		boolean autoDelete = false;
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
		// 把队列和交换机绑定
		channel.queueBind(queueName,
				exchangeName, 
				"book");

		// 设置每次取几个数据
		channel.basicQos(1);
		// 4,得到消费者
		// 创建的consumer通过channel来读数据
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 去取数据
		// p2:autoack 开启手动确认
		boolean autoAck = false;
		channel.basicConsume(queueName, autoAck, consumer);
		// 5,遍历消费者consumer
		boolean isRunning = true;
		System.out.println("消费者2启动了");
		while (isRunning) {
			// delivery代表的是消息队列中的一个数据
			Delivery delivery = consumer.nextDelivery();
			byte[] body = delivery.getBody();
			String msg = new String(body);
			System.out.println("消费者2收到：" + msg + "发书");
			// 不发送确认信息，服务器上能看到队列中的消息
			long deliveryTag = delivery.getEnvelope().getDeliveryTag();
			channel.basicAck(deliveryTag, true);
		}
		// 6,连接关闭
		channel.close();
		connection.close();
	}

}
