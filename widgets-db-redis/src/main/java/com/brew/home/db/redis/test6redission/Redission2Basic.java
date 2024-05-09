package com.brew.home.db.redis.test6redission;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/5/8 10:08
 */
public class Redission2Basic {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        //1. 字符串操作
        RBucket<String> rBucket =  redissonClient.getBucket("key-1");
        rBucket.set("val-1", 1, TimeUnit.SECONDS);
        System.out.println(rBucket.get());
        TimeUnit.SECONDS.sleep(2);
        System.out.println(rBucket.get());

        //2.字符串操作-2 直接存储对象
        //实际存储的格式："\x00\x01*com.brew.home.db.redis.test6redission.User\xf7\x80\xde\x00\xfc\x041111\x00"
        //所以，跟用spring redisTemplate直接缓存一个User时存在的问题是一样的：这个类的package结构动不了了
        RBucket<User> rBucket2 =  redissonClient.getBucket("key-2");
        rBucket2.set(new User("1111", 222), 10, TimeUnit.SECONDS);
        System.out.println(rBucket2.get().getName() + ":::" +rBucket2.get().getAge());
        TimeUnit.SECONDS.sleep(12);
        System.out.println(rBucket2.get());

        //3. map操作
        RMap<String, String> rMap = redissonClient.getMap("key-map");
        rMap.put("id", "123");
        rMap.put("name", "赵四");
        rMap.put("age", "50");
        rMap.expire(30, TimeUnit.SECONDS);
        System.out.println(redissonClient.getMap("key-map").get("name"));

        //4. 列表操作
        //RList<String> rList = redissonClient.getList("listkey");
        //....

        //5. 集合操作
        //RSet<String> rSet = redissonClient.getSet("setkey");
        //....

        //6. 有序集合操作
        //RSortedSet<String> sortSetkey = redissonClient.getSortedSet("sortSetkey");
        //....

        redissonClient.shutdown();
    }

}
