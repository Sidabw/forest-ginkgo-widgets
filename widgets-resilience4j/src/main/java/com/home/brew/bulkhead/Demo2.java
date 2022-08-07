package com.home.brew.bulkhead;

import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;

public class Demo2 {
    public static void main(String[] args) {

        //固定大小线程池 + 有界队列，完成隔离，并发请求时哪些可以执行，哪些超限了就不让执行
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(10)
                .coreThreadPoolSize(2)
                .queueCapacity(20)
                .build();

        ThreadPoolBulkhead bulkhead = ThreadPoolBulkhead.of("name", config);


//        CompletionStage<String> supplier = ThreadPoolBulkhead
//                .executeSupplier(bulkhead, backendService::doSomething);
    }
}
