package com.home.brew.p5timelimiter;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Demo1 {

    public static void main(String[] args) throws Exception {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .cancelRunningFuture(true)
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        // 使用自定义的全局配置创建一个TimeLimiterRegistry
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.of(config);

        //registry使用默认的配置创建一个TimeLimiter
        TimeLimiter timeLimiter = timeLimiterRegistry.timeLimiter("name1");

        // 需要一个调度器对非阻塞CompletableFuture进行调度，控制超时时间
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        //方式1
        // 返回CompletableFuture类型的非阻塞变量
//        CompletableFuture<String> result = timeLimiter.executeCompletionStage(
//                scheduler, () -> CompletableFuture.supplyAsync(helloWorldService::sayHelloWorld)).toCompletableFuture();

        //方式2
        // 阻塞方式，实际上是调用了future.get(timeoutDuration, MILLISECONDS)
        //ps: 那干嘛还用你这个time limiter
        String result = timeLimiter.executeFutureSupplier(
                () -> CompletableFuture.supplyAsync(Demo1::sayHelloWorld));
        System.out.println(result);

    }

    private static String sayHelloWorld() {
        try {

            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("ni caicai");
        }
        return "success";
    }
}
