package com.brew.home.guava.string;

import com.google.common.base.Splitter;

import java.util.Map;

/**
 * @author shaogz
 */
public class LearnSplitter {

    public static void main(String[] args) {
        /*
        Splitter 能将一个字符串按照分隔符生成字符串集合，是 Joiner的反向操作。
        常用方法如下：
        on(String)：静态工厂方法，生成一个新的 Splitter 对象，参数为连接符
        trimResults()：结果去除子串中的空格
        omitEmptyStrings()：去除null的子串
        split(String)：拆分字符串
        withKeyValueSeparator(String)：得到 MapSplitter，拆分成Map的键、值。注意，这个对被拆分字符串格式有严格要求，否则会抛出异常
         */
        String string = " ,a,b,";
        System.out.println(Splitter.on(",").split(string));
        System.out.println(Splitter.on(",").trimResults().split(string));
        System.out.println(Splitter.on(",").omitEmptyStrings().split(string));
        System.out.println(Splitter.on(",").trimResults().omitEmptyStrings().split(string));

        // 根据长度拆分
        string = "12345678";
        System.out.println(Splitter.fixedLength(2).split(string));

        // 拆分成Map
        Map<String, String> split = Splitter.on("#").withKeyValueSeparator(":").split("1:2#3:4");
        System.out.println(split);
    }
}
