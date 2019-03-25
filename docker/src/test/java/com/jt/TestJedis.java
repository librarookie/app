package com.jt;

import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chao
 * @Date 2019/3/14 - 19:44
 */
public class TestJedis {

    @Test    //完成单实例链接,修改代码中ip地址
    public void jedis(){
        Jedis jedis = new Jedis("192.168.220.148", 7000);
        //jedis.auth("123456");
        jedis.set("name", "tony");	//调用redis命令set
        String s = jedis.get("name");
        System.out.println(s);
        jedis.close();
    }

    //redis测试分片
    @Test
    public void shard(){
        //2.创建分片的连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(500); // 最大连接数
        poolConfig.setMaxIdle(20);   // 最大闲置连接
        //3.准备redis的分片
        List<JedisShardInfo> shards = new ArrayList<>();
        String host = "192.168.220.148";
        shards.add(new JedisShardInfo(host, 7000));
        shards.add(new JedisShardInfo(host, 7001));
        shards.add(new JedisShardInfo(host, 7002));
        //1.创建分片的对象
        ShardedJedisPool jedisPool =
                new ShardedJedisPool(poolConfig, shards);
        //获取jedis对象
        ShardedJedis shardedJedis = jedisPool.getResource();
        //5.redis的存取值操作
        for (int i = 0; i < 9; i++) {
            shardedJedis.set("n"+i,"我是分片操作"+i);
        }
    }
}
