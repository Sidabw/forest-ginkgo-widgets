package com.home.brew.p3ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

import java.time.Duration;


public class Demo1 {

    public static void main(String[] args) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                //在一次刷新周期内，允许执行的最大请求数
                .limitForPeriod(2)
                //限流器每隔limitRefreshPeriod刷新一次，将允许处理的最大请求数量重置为limitForPeriod
                .limitRefreshPeriod(Duration.ofSeconds(100))
                //线程等待权限的默认等待时间
                .timeoutDuration(Duration.ofMillis(25))
                .build();
        // 创建Registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        // 使用registry
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("name1");

        // 使用限流器装饰BackendService.doSomething()
        CheckedRunnable restrictedCall = RateLimiter.decorateCheckedRunnable(rateLimiter, () -> {
                    System.out.println("I'm here, .");
                });


        for (int i = 0; i<3;i++) {
            Try.run(restrictedCall)
//                .andThenTry(restrictedCall)
                    .onFailure(throwable -> {
                        System.out.println("fail");
                    });
        }
    }

}
