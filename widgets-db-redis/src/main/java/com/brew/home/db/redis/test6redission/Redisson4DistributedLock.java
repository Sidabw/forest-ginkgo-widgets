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

        //示例一：启用看门狗，对应业务代码执行时间过长，必须一直持有锁（即锁续期）的场景。
//        test1();

        //示例二：禁用看门狗，手动指定锁释放时间（leaseTime）。该case下，unlock可以不写，但是建议必须写上。
//        test2();

        //示例三：tryLock
        test3();

        //示例四：错误代码示例
//        test4();

    }

    private static void test1() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock-test");
        try {
            //注意：如果想启用看门狗机制，则不能手动指定leaseTime；
            //不启用看门狗的示例：lock.lock(25, TimeUnit.SECONDS);

            //默认启用看门狗，即分布式锁的自动续期
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
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock-test");
        try {
            //禁用看门狗，手动指定锁释放时间（leaseTime）
            lock.lock(12, TimeUnit.SECONDS);
            System.out.println("lock success");
            TimeUnit.SECONDS.sleep(25);
            System.out.println("业务代码执行完毕");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            //unlock可以不写，但是建议必须写上。
            System.out.println("unlock success");
        }
    }

    private static void test3() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock-test");
        try {
            //同ReentrantLock用法一致，尝试获取锁，5s后仍获取不到的话返回false
            //因为指定了leaseTime，所以看门狗是禁用状态，
            //进而，unlock可以省略，但是建议必须加上！
            boolean acquireLockSuccess = lock.tryLock(5, 12, TimeUnit.SECONDS);
            System.out.println("lock success : " + acquireLockSuccess);
            TimeUnit.SECONDS.sleep(45);
            System.out.println("业务代码执行完毕");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
            System.out.println("unlock success");
        }
    }

    private static void test4() {
        new Thread(()-> {
            new Thread(()->{

                Config config = new Config();
                config.useSingleServer().setAddress("redis://localhost:6379");
                // 相当于创建了redis的连接
                RedissonClient redissonClient = Redisson.create(config);
                RLock lock = redissonClient.getLock("lock-test");
                try {
                    //测试结论：启用看门狗的情况下，必须在finally里unlock
                    //测试方法：不断的看ttl
                    lock.lock();
                    System.out.println("lock success");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("业务代码执行完毕");
                    throw new RuntimeException("xxx");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    //没有unlock
                    System.out.println("finally 执行完毕，未手动unlock");
                }

            }).start();
        }).start();

    }
}
