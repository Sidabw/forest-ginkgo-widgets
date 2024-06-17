# redisson

### redis原生命令映射redis的类
[redis commands map redisson class](https://github.com/redisson/redisson/wiki/11.-Redis-commands-mapping)

### 看门狗机制
在redisson看门狗机制出现之前，获取一个分布式锁的标准方式是：把setnx命令和expire命令放到一个lua里。
该标准的唯一缺陷，业务代码极端情况下没执行完，但是锁被释放掉了。
redisson的看门狗，则是默认给30s的ttl，然后每10s续期一次，续期一次ttl+10。该机制很好的解决了上面「标准方式」的缺陷。
每10s续期一次的实现，是一个时间轮+延迟队列

### 其他参考

* 印象笔记/缓存穿透-布隆过滤器
