# AOP in aspectJ
> ajc 即 aspectJ

## 编译时织入
* 使用pom里的 aspectj-maven-plugin
* 打包时，别忘了指定启动类：maven-shade-plugin

## 加载时织入（LTW）
* 注释掉pom里的 aspectj-maven-plugin
* 编写resources/META-INF/aop.xml
* 打包
* 使用javaagent运行： 
```
  java -javaagent:/Users/feiyi/Documents/worktools-bokecc/aspectJ-dependencies/aspectjweaver-1.8.9.jar -jar target/widgets-aop-1.0-SNAPSHOT.jar
```


