## 测试步骤
* 查找系统redis-server进程，kill掉（don’t ask why）
* 启动redis6或更高版本
* 使用redis-cli连接，set user 1111
* 启动ClientSideCachingDemo.main
* 使用redis-cli连接，set user 2222
* 此时ConnectorMap中会收到驱逐的通知

## 实战参考
* https://redis.io/docs/latest/develop/use/client-side-caching/
* https://juejin.cn/post/7120148572846178335
* https://colobu.com/2020/05/02/redis-client-side-caching/

## hydra系列参考
* [redis的client-side-cache简单介绍](https://segmentfault.com/a/1190000040926742) redis-server必须是支持RESP3的redis6.0及以上版本。 三中模式，普通、广播、转发。用的比较多的是广播模式，被配置指定key前缀订阅。普通模式比较占redis-server内存，因为要存储key和客户端id，以便在指定key失效时通知指定客户端；转发模式主要给不方便升级redis-client版本的客户端用的，算是对RESP-v2的一种兼容
* [redis的client-side-cache简单介绍-2](https://www.cnblogs.com/remcarpediem/p/12872053.html)（同上三种模式的介绍；基数树❓）
* [redis的client-side-cache简单介绍-3](https://juejin.cn/post/7119016087596826632)

## redis通信协议RESP介绍
* [redis通信协议RESPV2 介绍](https://mp.weixin.qq.com/s?__biz=MzIwMTgzOTQ0Ng==&mid=2247485834&idx=1&sn=482e312cc0dc7d2c78f330e1bda81be3&scene=21#wechat_redirect)（比较简单，位于TCP协议之上。我们使用redis-cli与redis-server通信时请求/响应的实际内容几乎就是这个协议的大部分内容了，不像HTTP那样各种method/code/header…我们要的就是简单、快、易读。）
* [redis 通信协议 RESP3 介绍](https://mp.weixin.qq.com/s/rLk2EW0TKAIkQvx1WefafA)
  （相比 RESPV2 肯定功能更强大，支持的数据类型更多，但更重要的是为 redis-client-side-cache 需要的服务端主动 push 来驱逐缓存提供了协议支持！）

# redis+caffeine做二级缓存
* [redis+caffeine做二级缓存-1](https://juejin.cn/post/7116768866956476429)（总体来说没啥新意..    考虑通用性的话，用自定义注解+切面处理redis+caffeine的get/put/delete逻辑的方案是比较好的。但ams那种手动指定CacheManager的方式，更直观吧。）
* [redis+caffeine做二级缓存-2](https://juejin.cn/post/7117497031714865159)（通过自定义CacheManager实现二级缓存的完美接入；至于分布式环境的问题还是redis的pub/sub。）