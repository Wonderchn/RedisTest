package com.hongna.JedisDemo;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Demo01 {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        String pong = jedis.ping();
        System.out.println(pong);

        

    }


    /**
     * 通过Jedis获取key值
     */
    @Test
    public void testKey(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
       jedis.set("k1", "v1");
       jedis.set("k2","v2");
       jedis.set("k3", "v3");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys.size());
        for (String key : keys){
            System.out.println(key);
        }
        System.out.println(jedis.exists("k1"));
        System.out.println(jedis.ttl("k1"));

    }

    @Test
    public void testString(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        jedis.mset("str1","v1","str2","v2","str3","v3");
        System.out.println(jedis.mget("str1","str2","str3"));
    }

    @Test
    public void testList(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        jedis.rpush("mylist", "xixi","ajaja");

        List<String> list = jedis.lrange("mylist", 0, -1);
        for (String element : list){
            System.out.println(element);
        }
    }

    @Test
    public void testSet(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        jedis.sadd("orders", "order02");
        jedis.sadd("orders", "order03");
        jedis.sadd("orders", "order04");
        Set<String> smembers = jedis.smembers("orders");
        for (String order : smembers) {
            System.out.println(order);
        }
        jedis.srem("orders", "order02");

    }

    @Test
    public void testHash(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        jedis.hset("hash1", "username","lisi");
        System.out.println(jedis.hget("hash1", "username"));
        HashMap<String, String> map = new HashMap<>();
        map.put("telphone", "1111111111111");
        map.put("name", "xiaochen66");
        map.put("email", "xiaoji@ssss");
        jedis.hmset("hash2", map);
        List<String> result = jedis.hmget("hash2", "telphone", "email");
        for (String element : result){
            System.out.println(element);
        }
    }


    @Test
    public void testZset(){
        Jedis jedis = new Jedis("192.168.138.128",6379 );
        jedis.zadd("zset", 100d, "z3");
        jedis.zadd("zset", 90d, "l4");
        Set<String> zset = jedis.zrange("zset", 0, -1);
        for (String e : zset){
            System.out.println(e);
        }
    }
}