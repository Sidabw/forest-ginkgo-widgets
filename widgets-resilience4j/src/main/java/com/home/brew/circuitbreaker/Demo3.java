package com.home.brew.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 跟Demo1一摸一样，catch异常的地方改成了recover, 执行逻辑、结果都一样。Try的recover不影响断路器的相关逻辑。
 */
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(2)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(Throwable.class)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name");


        for (int i = 0; i< 20; i++) {
            System.out.println("I'm coming..");
            //方式二
            Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, Demo3::doSomething);
            //从异常中恢复：'doSomething'抛异常了，此时返回 'Hello from Recovery'，而不是把异常抛出来
            String result = Try.ofSupplier(decoratedSupplier).recover(throwable -> "Hello from Recovery").get();
            /*
            * 只是测试代码，没有很复杂的逻辑去用Try的 .filter().andThenTry().andThenTry().andFinally()....
            * */
            System.out.println("I'm done... " + result);

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
            throw new RuntimeException("xxx");
        }
        return "success?" + success;
    }
}
