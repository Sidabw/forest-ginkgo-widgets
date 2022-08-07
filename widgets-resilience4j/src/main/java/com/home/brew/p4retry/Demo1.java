package com.home.brew.p4retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.io.IOException;
import java.time.Duration;

public class Demo1 {

    public static void main(String[] args) {
        RetryConfig config = RetryConfig.custom()
                //最大重试次数
                .maxAttempts(3)
                //两次重试之间的时间间隔
                .waitDuration(Duration.ofMillis(1000))
                //
//                .retryOnResult(response -> response.getStatus() == 500)
//                .retryOnException(e -> e instanceof WebServiceException)
                //配置用于计算是否应重试的断言。如果要重试，断言必须返回true，否则返回false。
//                .retryOnResultPredicate
                //配置一个断言，判断某个异常发生时，是否要进行重试。如果要重试，断言必须返回true，否则必须返回false。
                //.retryOnExceptionPredicate
                .retryExceptions(IOException.class, RuntimeException.class)
//                .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        // 使用自定义的配置创建RetryRegistry
        RetryRegistry registry = RetryRegistry.of(config);

        // 使用默认的配置从Registry中获取和创建一个Retry
        Retry retry = registry.retry("name1");

        // 使用Retry对HelloWorldService进行装饰
        CheckedFunction0<String> retryableSupplier = Retry
                .decorateCheckedSupplier(retry, Demo1::sayHelloWorld);

        // 进行方法调用
        Try<String> result = Try.of(retryableSupplier)
                .recover((throwable) -> "Hello world from recovery function");
        System.out.println(result.get());
    }

    static int i = 0;
    private static String sayHelloWorld(){
        //前两次抛异常
        if (i++ > 1) {
            return "success";
        }
        System.out.println("fail");
        throw new RuntimeException("xxxxx");
    }
}
