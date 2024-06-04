package com.brew.home.db.redis.test6redission;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author shaogz
 * @since 2024/5/7 19:32
 */
public class Redisson1BloomFilter {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        // 相当于创建了redis的连接
        RedissonClient redisson = Redisson.create(config);

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("myBloomFilter");
        //初始化,预计元素数量为100000000,期待的误差率为4%
        bloomFilter.tryInit(100000000,0.01);
        //将号码12345插入到布隆过滤器中
        bloomFilter.add("12345");

        System.out.println(bloomFilter.contains("123456"));//false
        System.out.println(bloomFilter.contains("12345"));//true

        System.out.println("---");
        System.out.println(Long.MAX_VALUE);
    }

}
