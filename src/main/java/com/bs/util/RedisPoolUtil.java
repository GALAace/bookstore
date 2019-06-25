package com.bs.util;

import com.bs.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @Auther: 杨博文
 * @Date: 2019/6/20 23:48
 */

@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        }
        catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
        }
        finally {
            if(jedis!=null){
                jedis.close();
            }
            return result;
        }
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }
        catch (Exception e) {
            log.error("set key:{} error",key,e);
        }
        finally {
            if(jedis!=null){
                jedis.close();
            }
            return result;
        }
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        }
        catch (Exception e) {
            log.error("del key:{}  error",key,e);
        }
        finally {
            if(jedis!=null){
                jedis.close();
            }
            return result;
        }
    }

    /**
     * @Description: 将值 value 关联到 key ，并将 key 的生存时间exTime单位：s
     * @Auther: 杨博文
     * @Date: 2019/6/21 0:36
     */
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        }
        catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
        }
        finally {
            if(jedis!=null){
                jedis.close();
            }
            return result;
        }
    }
    /**
     * @Description: 设置key有效期，exTime单位：s
     * @Auther: 杨博文
     * @Date: 2019/6/21 0:38
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        }
        catch (Exception e) {
            log.error("expire key:{} error",key,e);
        }
        finally {
            if(jedis!=null){
                jedis.close();
            }
            return result;
        }
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","value");

        String value = RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyex","valueex",60*10);

        RedisPoolUtil.expire("keyTest",60*20);

        RedisPoolUtil.del("keyTest");

        System.out.println("end");


    }

}
