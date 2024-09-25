package com.brew.home;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TrackingArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.caching.CacheAccessor;
import io.lettuce.core.support.caching.CacheFrontend;
import io.lettuce.core.support.caching.ClientSideCaching;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shaogz
 * @since 2024/7/11 15:30
 */
public class ClientSideCachingDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建 RedisClient 连接信息
        RedisURI redisURI= RedisURI.builder()
                .withHost("127.0.0.1")
                .withPort(6379)
                .build();
        RedisClient client = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = client.connect();

        Map<String, String> map = new HashMap<>();
        CacheFrontend<String,String> frontend= ClientSideCaching.enable(CacheAccessor.forMap(map),
                connect, TrackingArgs.Builder.enabled().noloop());

        String key="user";
        while (true){
            System.out.println("map.get : " + map.get(key));
            System.out.println("frontend.get : " + frontend.get(key));
            TimeUnit.SECONDS.sleep(10);
        }
    }

}
