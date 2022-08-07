# 隔离机制

> 用于'被请求方'；如果说有一个很核心的函数，我们希望控制它被并发调用的次数。那么就需要使用隔离-bulkhead

* SemaphoreBulkhead使用了信号量 【本来就是个信号量啊Semaphore，红绿灯，哪些可以过哪些不可以】，
* FixedThreadPoolBulkhead使用了有界队列和固定大小线程池 【】


