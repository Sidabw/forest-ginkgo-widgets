package com.brew.home.guava.string;

import com.google.common.base.Strings;

/**
 * @author shaogz
 */
public class LearnStrings {

    public static void main(String[] args) {
        /*
        Strings 类主要提供了对字符串的一些操作。主要方法如下：
        nullToEmpty(String string) ：null字符串转空字符串
        emptyToNull(String string)：空字符串转null字符串
        isNullOrEmpty(String string)：判断字符串为null或空字符串
        padStart(String string, int minLength, char padChar)：如果string的长度小于minLeng，在string前添加padChar，直到字符串长度为minLeng。
        String padEnd(String string, int minLength, char padChar)：和padStart类似，不过是在尾部添加新字符串
        commonPrefix(CharSequence a, CharSequence b)：返回共同的前缀
        commonSuffix(CharSequence a, CharSequence b)：返回共同的后缀
         */
        System.out.println(Strings.nullToEmpty(null));
        System.out.println(Strings.emptyToNull(""));
        System.out.println(Strings.isNullOrEmpty(""));
        System.out.println(Strings.padStart("abc", 10, 'e'));
        System.out.println(Strings.padEnd("abc", 5, 'f'));
        System.out.println(Strings.commonPrefix("aabcd123123123", "aabcd998798798"));
        System.out.println(Strings.commonSuffix("12312398798okokoko", "5666373736373736373637okokoko"));
    }

}
