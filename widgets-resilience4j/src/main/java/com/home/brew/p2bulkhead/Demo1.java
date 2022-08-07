package com.home.brew.p2bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo1 {

    public static void main(String[] args) throws InterruptedException {
        //为Bulkhead创建自定义的配置
        BulkheadConfig config = BulkheadConfig.custom()
                //隔离允许线程并发执行的最大数量
                .maxConcurrentCalls(2)
                //当达到并发调用数量时，新的线程执行时将被阻塞，这个属性表示最长的等待时间。
                .maxWaitDuration(Duration.ofMillis(500))
                .build();

        // 使用自定义全局配置创建BulkheadRegistry
        BulkheadRegistry registry = BulkheadRegistry.of(config);

        // 使用默认的配置从registry中创建Bulkhead
        Bulkhead bulkheadWithDefaultConfig = registry.bulkhead("name1");


        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        for (int i = 0; i< 5 ;i++) {
            new Thread(() -> {

                try {
                    //等到5个齐了，再一块执行
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                /*
                * 1。 先装饰被执行函数
                * 2。 用Try因为有recover，这样就不需要再手动的catch'被bulkhead拒绝的调用'抛出的异常了。
                * */
                CheckedFunction0<String> stringCheckedFunction0 = Bulkhead.decorateCheckedSupplier(bulkheadWithDefaultConfig, () -> {
                    try {
                        System.out.println(atomicInteger.getAndIncrement());
                        TimeUnit.MILLISECONDS.sleep(2000);
                    } catch (Exception e) {

                    }
                    return "I'm success";
                });
                String res = Try.of(stringCheckedFunction0).recover(throwable -> "i'm fail").get();
                System.out.println(res);
            }).start();
        }

        countDownLatch.countDown();
        TimeUnit.SECONDS.sleep(10);

    }
}
