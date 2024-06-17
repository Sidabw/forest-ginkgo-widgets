package com.brew.home.db.redis.test6redission;

import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/6/17 11:23
 */
public class Redisson4DistributedObj {

    public static void main(String[] args) throws InterruptedException {
        testCountDownLatch();
    }

    public static void testCountDownLatch() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redissonClient = Redisson.create(config);

        ExecutorService pool = Executors.newFixedThreadPool(20);
        //thread-1
        pool.execute(() -> {
            RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-latch");
            countDownLatch.trySetCount(2);
            try {
                countDownLatch.await();
                TimeUnit.SECONDS.sleep(2);
                System.out.println("await finished + threadId: " + Thread.currentThread().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        TimeUnit.SECONDS.sleep(2);
        //thread-2
        pool.execute(() -> {
            RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-latch");
            countDownLatch.countDown();
            System.out.println("count down finished + threadId: " + Thread.currentThread().getId());
        });
        //thread-3
        pool.execute(() -> {
            RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-latch");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println("count down finished + threadId: " + Thread.currentThread().getId());
        });
    }

    public static void testSemaphore() {
        //same to juc.Semaphore

        //RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        //semaphore.release();

//        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
//        semaphore.acquire();
    }

}
