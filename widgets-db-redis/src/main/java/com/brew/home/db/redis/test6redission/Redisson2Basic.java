package com.brew.home.db.redis.test6redission;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/5/8 10:08
 */
public class Redisson2Basic {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        //1. 字符串操作
        //opsString(redissonClient);

        //2.字符串操作-2 直接存储对象
        //实际存储的格式："\x00\x01*com.brew.home.db.redis.test6redission.User\xf7\x80\xde\x00\xfc\x041111\x00"
        //所以，跟用spring redisTemplate直接缓存一个User时存在的问题是一样的：这个类的package结构动不了了
        //opsObject(redissonClient);

        //3. map操作
        //opsMap(redissonClient);

        //4. 列表操作
        //opsList(redissonClient);

        //4.2 列表-双端队列操作（对应lpush rpop等）
        //opsDeque(redissonClient);

        //5. 集合操作
        //opsSet(redissonClient);

        //6. 有序集合操作
        opsSortedSet(redissonClient);

        redissonClient.shutdown();
    }

    private static void opsSortedSet(RedissonClient redissonClient) {
        RSortedSet<String> sortSetkey = redissonClient.getSortedSet("sortSetkey");
        sortSetkey.add("v1");
        sortSetkey.add("v3");
        sortSetkey.add("v5");
        sortSetkey.add("v7");
        Iterator<String> iterator = sortSetkey.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println(redissonClient.getSortedSet("sortSetkey"));
        //不能这么用
//        SortedSet<Object> objects = redissonClient.getSortedSet("sortSetkey").headSet("v6");
    }

    private static void opsSet(RedissonClient redissonClient) {
        RSet<String> rSet = redissonClient.getSet("setkey");
        rSet.add("v1");
        rSet.add("v2");
        rSet.add("v1");
        System.out.println(rSet.size());
    }

    private static void opsDeque(RedissonClient redissonClient) {
        RDeque<Object> rDeque = redissonClient.getDeque("dequekey");
        rDeque.addFirst("first-v");
        rDeque.addLast("last-v");
        System.out.println(rDeque.getFirst());
    }

    private static void opsList(RedissonClient redissonClient) {
        RList<String> rList = redissonClient.getList("listkey");
        rList.add("v0");
        rList.add("v1");
        System.out.println(rList.get(1));
    }

    private static void opsMap(RedissonClient redissonClient) {
        RMap<String, String> rMap = redissonClient.getMap("key-map");
        rMap.put("id", "123");
        rMap.put("name", "赵四");
        rMap.put("age", "50");
        rMap.expire(30, TimeUnit.SECONDS);
        System.out.println(redissonClient.getMap("key-map").get("name"));
    }

    private static void opsObject(RedissonClient redissonClient) throws InterruptedException {
        RBucket<User> rBucket2 =  redissonClient.getBucket("key-2");
        rBucket2.set(new User("1111", 222), 10, TimeUnit.SECONDS);
        System.out.println(rBucket2.get().getName() + ":::" +rBucket2.get().getAge());
        TimeUnit.SECONDS.sleep(12);
        System.out.println(rBucket2.get());
    }

    private static void opsString(RedissonClient redissonClient) throws InterruptedException {
        RBucket<String> rBucket =  redissonClient.getBucket("key-1");
        rBucket.set("val-1", 1, TimeUnit.SECONDS);
        System.out.println(rBucket.get());
        TimeUnit.SECONDS.sleep(2);
        System.out.println(rBucket.get());
    }

}
