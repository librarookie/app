package com.tedu.rabbitmq;

import java.util.HashMap;

import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test_1_simple_provider {
	
	//向消息队列写消息
	@Test
	public void provider() throws Exception
	{
		//1,连接服务器
		ConnectionFactory factory=
				new ConnectionFactory();
		factory.setHost("192.168.216.202");
		//浏览器访问rabbitmq后台管理用的是15672
		//发消息用的的5672
		//一个服务器可以有多个端口号，访问15672,服务器返回的是网页
		//访问5672,可以发消息，也可以取消息
		factory.setPort(5672);
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		factory.setVirtualHost("/jt");
		//2,得到channel
		//com.rabbitmq.client.connection
		Connection connection=factory
				.newConnection();
		Channel channel=connection.createChannel();
		
		//3,定义队列		
		//p1:队列名称
		String queueName="orderQueue1";
		//p2:durable true 持久化，保存到硬盘		
		boolean durable=true;
		//p3:exclusive false 别的程序也能访问
		boolean exclusive=false;		
		//p4:autoDelete false 队列中的消息处理完了，不删队列
		boolean autoDelete=false;		
		//p5:arguments 队列的配置信息
		HashMap<String,Object> argments=null;
		channel.queueDeclare(queueName, durable, exclusive, 
				autoDelete, argments);
		//4,写消息		
		//p1:exchange 给"",使用的是default exchange
		String exchange="";
		//p2:routing 路由 key关键字，决定消息放到那个队列
		String routingKey=queueName;		
		//p3:props 是property
		//com.rabbitmq.client.AMQP.BasicProperties
		//BasicProperties properties=null;
		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
		//消息持久化
		builder.deliveryMode(2);
		AMQP.BasicProperties properties = builder.build();

		byte[] body="msg6".getBytes();
		
		//p4:body是消息内容
		channel.basicPublish(exchange, routingKey,
				properties, body);
//		while(true){
//			
//		}
		//5,关闭连接
		channel.close();
		connection.close();
		//System.out.println("发送了"+msg);
	}

}
