package com.hongna.JedisDemo;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * 有这么一个请求，我们需要实现手机验证码的实现,并实现最多120秒过期
 * 并且用户一天最多点击3次
 *
 */
public class PhoneCode {
    public static void main(String[] args) {
        //生成随机验证码
        String code = getCode();
        Map map = saveKeys(code, "123456");
        if ( map.get("msg") .equals(1)){
            System.out.println("正确输出");
        }
    }

    public static Map saveKeys(String code,String phoneNumber){
        Map map = new HashMap();

        Jedis jedis = new Jedis("192.168.138.128",6379 );
        String countKey = "VerifyCode" + phoneNumber + ":count";
        String codeKey = "VerifyCode" + phoneNumber + "code";
        String count = jedis.get(countKey);
        if (count == null){
            //没有发送过，第一次发送
            jedis.setex(countKey, 24*60*60, "1");

        }
        else if (Integer.parseInt(count) <=2){
            jedis.incr(countKey);
        }
        else if (Integer.parseInt(count) >2){
            System.out.println("今天发送次数已经超过3次");
            map.put("msg", -1);
            jedis.close();
            return map;
        }

        //发送验证码到redis里面
        String vcode = getCode();
        jedis.setex(codeKey, 120, vcode);
        jedis.close();
        map.put("msg", 1);
        return map;
    }

    //生成随机验证码
    public static String getCode(){
        Random random = new Random();
        String code = "";
        for (int i = 0 ; i < 6; i++){
            int i1 = random.nextInt(10);
            String s = String.valueOf(i1);
            code  += s ;

        }
        return code;
    }
}
