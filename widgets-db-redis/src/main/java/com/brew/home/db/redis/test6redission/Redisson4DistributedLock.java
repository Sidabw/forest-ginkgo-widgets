package com.brew.home.db.redis.test6redission;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/5/8 11:38
 */
public class Redisson4DistributedLock {

    public static void main(String[] args) throws InterruptedException {
//        test1();
        test2();

        TimeUnit.SECONDS.sleep(40);
    }

    private static void test1() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock-test");
        try {
            //如果想启用看门狗机制，则不能手动指定leaseTime；不启用看门狗：lock.lock(25, TimeUnit.SECONDS);

            //ttl 命令会返回30，之后会变成29、28、27...21、然后再变成30
            //也就是每10s进行一次锁续期
            lock.lock();
            System.out.println("lock success");
            TimeUnit.SECONDS.sleep(45);
            System.out.println("业务代码执行完毕");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
            System.out.println("unlock success");
        }
    }

    private static void test2() {
        //测试结论：必须在finally里unlock
        //不存在：线程执行完毕了就自动unlock
        //不存在：限制执行异常了就自动unlock
        //测试方法：不断的看ttl

        new Thread(()-> {
            new Thread(()->{

                Config config = new Config();
                config.useSingleServer().setAddress("redis://localhost:6379");
                // 相当于创建了redis的连接
                RedissonClient redissonClient = Redisson.create(config);
                RLock lock = redissonClient.getLock("lock-test");
                try {
                    //如果想启用看门狗机制，则不能手动指定leaseTime；不启用看门狗：lock.lock(25, TimeUnit.SECONDS);

                    //ttl 命令会返回30，之后会变成29、28、27...21、然后再变成30
                    //也就是每10s进行一次锁续期
                    lock.lock();
                    System.out.println("lock success");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("业务代码执行完毕");
                    throw new RuntimeException("xxx");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    System.out.println("finally 执行完毕，未手动unlock");
                }

            }).start();
        }).start();

    }

}
