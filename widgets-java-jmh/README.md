# JMH
> 参考：https://www.zhihu.com/question/276455629/answer/1259967560

## 第一次基础学习
> 涉及到多线程的都没有实际操作

* p1-part1： 预热，相关预热迭代执行也会在这个时候操作完

* p1-part2 ：当前param下，每次迭代的执行耗时 time/ops

* p1-part3 ：当前param下，所有迭代的平均值

* ... 之后part2 & part3 会根据@Param配置了多少个，每个@Benchmark就会执行多少次。
Demo1每个@Benchmark方法会执行三次；

* p2 总结果： length就是@param，cnt应该是迭代次数，score + unit 就是 time/ops，Error+ unit是个误差。

