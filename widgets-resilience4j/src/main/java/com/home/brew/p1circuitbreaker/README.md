# 断路器
> 当失败率等于或大于阈值时，断路器状态并关闭变为开启，并进行服务降级.

> 应用：通常用户客户端发起请求时，加一层断路器，防止下游系统故障时，上游跟牵连收到影响。一直hang着。  

* 参考1：https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/core-modules/CircuitBreaker.md
* 参考2：https://blog.csdn.net/netyeaxi/article/details/104237289
* img/c1 下是断路器四种状态的互转，很重要
* 其他限流参考印象笔记

