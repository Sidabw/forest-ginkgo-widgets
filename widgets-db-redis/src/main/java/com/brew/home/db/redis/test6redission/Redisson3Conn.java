package com.brew.home.db.redis.test6redission;

/**
 * @author shaogz
 * @since 2024/5/8 11:35
 */
public class Redisson3Conn {


    /*
主要是对链接redis的方式做一个备忘：单点、主从、哨兵、集群

//集群模式
Config config = new Config();
// 集群状态扫描间隔时间，单位是毫秒
config.useClusterServers().setScanInterval(2000)
.addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
.addNodeAddress("redis://127.0.0.1:7002");
RedissonClient redisson = Redisson.create(config);


//哨兵模式
Config config = new Config();
config.useSentinelServers()
.setMasterName("mymaster")
//可以用"rediss://"来启用SSL连接
.addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379")
.addSentinelAddress("127.0.0.1:26319");
RedissonClient redisson = Redisson.create(config);


//主从
Config config = new Config();
config.useMasterSlaveServers()
//可以用"rediss://"来启用SSL连接
.setMasterAddress("redis://127.0.0.1:6379")
.addSlaveAddress("redis://127.0.0.1:6389", "redis://127.0.0.1:6332", "redis://127.0.0.1:6419")
.addSlaveAddress("redis://127.0.0.1:6399");
RedissonClient redisson = Redisson.create(config);
     */
}
