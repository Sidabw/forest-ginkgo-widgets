package com.home.brew.p1circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 本测试的执行路径：
 * <p> 前提1：当前CircuitBreakerConfig: 窗口大小、阈值、 断路器从开启过渡到半开应等待的时间、半开情况下允许通过的请求数
 * <p> 前提2：被调用函数：前三次失败，第四次及以后都是成功
 * <p> 结果1：'doSomething'被调用两次，都失败，此时断路器打开
 * <p> 结果2：'doSomething'再调用都不再真正执行，四次后，触发'waitDurationInOpenState'，断路器进入半开状态
 * <p> 结果3：'permittedNumberOfCallsInHalfOpenState(2)' 半开情况下允许通过的请求次数，此时'doSomething'真正执行2次，
 * 一次失败，一次成功，统计结果高于设定的阈值，此时断路器回到全开状态
 * <p> 结果4：'doSomething'再调用都不再真正执行，四次后，触发'waitDurationInOpenState'，断路器再次进入半开状态
 * <p> 结果5：....，此时'doSomething'真正执行2次，全部成功，统计结果低于设定的阈值，此时断路器退回到关闭状态
 * <p> 结果5：'doSomething'后续执行全部正常，断路器维持关闭下状态
 */
public class Demo1 {

    public static void main(String[] args) throws InterruptedException {
        // 为断路器创建自定义的配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                //窗口大小为2，阈值50%，即两个请求里有1个失败，就打开断路器
                .failureRateThreshold(50)
                .slidingWindowSize(2)
                //断路器从开启过渡到半开应等待的时间。
                .waitDurationInOpenState(Duration.ofMillis(10000))
                //半开情况下允许通过的请求数。这些请求完事后，超过阈值则回到全开，低于阈值则降低到关闭。
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(Throwable.class)
                //要记录哪些异常，不过话说，这些CheckedException都不可能抛出来啊...
//                .recordExceptions(IOException.class, TimeoutException.class)
                //排除哪些异常
//                .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        // 使用自定义的全局配置创建CircuitBreakerRegistry
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        //
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name");

        //方式一
        //此时可以看到，调用失败一次【executeSupplier抛出异常】后，后续的调用断路器直接抛异常，不再调用真正的方法
        //CallNotPermittedExceptionCircuitBreaker

        for (int i = 0; i< 20; i++) {
            try {
                System.out.println("I'm coming..");
                String result = circuitBreaker.executeSupplier(Demo1::doSomething);
                System.out.println("I'm done... " + result);
            } catch (Throwable e1) {
                //ignore
                System.out.println(e1.getClass() + e1.getMessage());
            }

            TimeUnit.SECONDS.sleep(2);
            System.out.println();
            System.out.println();
        }

//        //方式二
//        Supplier<String> decoratedSupplier = CircuitBreaker
//                .decorateSupplier(circuitBreaker, Demo1::doSomething);
//
//        String result = Try.ofSupplier(decoratedSupplier)
//                .recover(throwable -> "Hello from Recovery").get();
    }

    static int i = 0;
    private static String doSomething() {
        //前三次失败，第四次成功
        boolean success = i++ > 2;
        System.out.println("real doSomething , and i'm success ?  " + success);
        if (!success) {
            throw new RuntimeException("xxx");
        }
        return "success?" + success;
    }
}
