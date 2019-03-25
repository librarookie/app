package com.tedu.rabbitmq;

import java.util.HashMap;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Test_1_simple_consumerAck {
	// 从消息队列服务器取消息
	@Test
	public void consumer() throws Exception {
		// 1.建立连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.216.202");
		factory.setPort(5672);
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		factory.setVirtualHost("/jt");
		// 2,得到channel
		// com.rabbitmq.client
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 3,创建队列
		// 读消息时队列名称和写消息的队列名称必须一致
		// 如果队列存在，不创建，服务器不会再创建新的了
		// p1:队列名称
		String queueName = "orderQueue1";
		// p2:durable true 持久化，保存到硬盘
		boolean durable = true;
		// p3:exclusive false 别的程序也能访问
		boolean exclusive = false;
		// p4:autoDelete false 队列中的消息处理完了，不删队列
		boolean autoDelete = false;
		// p5:arguments 队列的配置信息
		HashMap<String, Object> argments = null;
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, argments);

		// 4,创建消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// autoAck:自动回复消息
		boolean autoAck=false;
		channel.basicConsume(queueName, autoAck, consumer);
		// 5,取消息
		while (true) {
			Delivery delivery = consumer.nextDelivery();
			byte[] data = delivery.getBody();
			String mString = new String(data);
			System.out.println("消费者取到：" + mString);
			try {
				
				long deliveryTag=delivery.getEnvelope().getDeliveryTag();
				System.out.println("deliveryTag="+deliveryTag);
				channel.basicAck(deliveryTag, true);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}

		// 6,连接关
		// channel.close();
		// connection.close();
		// System.out.println("end");
	}

}
