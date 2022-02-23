package com.brew.home.guava.string;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shaogz
 */
public class LearnJoiner {

    public static void main(String[] args) {
        /*
        用分隔符将多个字符串（或数组元素）连接成一个字符串。
        常用方法如下：
        on(String)：静态工厂方法，生成一个新的 Joiner 对象，参数为连接符
        skipNulls()：如果元素为空，则跳过
        useForNull(String)：如果元素为空，则用这个字符串代替
        join(数组/链表)：要连接的数组/链表
        appendTo(String,数组/链表)：在第一个参数后面新加上 拼接后的字符串
        withKeyValueSeparator(String)：得到 MapJoiner，连接Map的键、值
         */
        List<String> list1 = Arrays.asList("aa", "bb", "cc");
        System.out.println(Joiner.on("-").join(list1));

        List<String> list2 = Arrays.asList("aa", "bb", "cc", null, "dd");
        System.out.println(Joiner.on("-").skipNulls().join(list2));
        System.out.println(Joiner.on("-").useForNull("nulla").join(list2));

        Map<String, String> map = ImmutableMap.of("k1", "v1", "k2", "v2");
        System.out.println(Joiner.on("-").withKeyValueSeparator("=").join(map));
    }
}
