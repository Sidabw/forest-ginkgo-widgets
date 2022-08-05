package com.home.brew.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 基于Demo1，但是是按照'慢调用'判定方法调用是否失败。代码逻辑除以下地方外，同Demo1完全一样。包括执行结果也一样。
 * <p> .failureRateThreshold(50)删掉了，改为.slowCallRateThreshold(50)
 * <p> .slowCallDurationThreshold(Duration.ofMillis(500))
 * <p> 'doSomething' 不抛异常了，睡一会，等待被判定'慢调用'
 */
public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(2)
                //慢调用阈值，50%
                .slowCallRateThreshold(50)
                //方法调用时间超过500ms就算'慢调用'
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(Throwable.class)
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
                String result = circuitBreaker.executeSupplier(Demo2::doSomething);
                System.out.println("I'm done... " + result);
            } catch (Throwable e1) {
                //ignore
                System.out.println(e1.getClass() + e1.getMessage());
            }

            TimeUnit.SECONDS.sleep(2);
            System.out.println();
            System.out.println();
        }
    }

    static int i = 0;
    private static String doSomething() {
        //前三次失败，第四次成功
        boolean success = i++ > 2;
        System.out.println("real doSomething , and i'm success ?  " + success);
        if (!success) {
            try {
                TimeUnit.MILLISECONDS.sleep(600);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return "success?" + success;
    }

}
