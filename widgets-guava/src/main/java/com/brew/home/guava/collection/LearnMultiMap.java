package com.brew.home.guava.collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.Test;

import java.util.Collection;

/**
 * @author shaogz
 */
public class LearnMultiMap {

    public static void main(String[] args) {
        /*
         通俗来讲，Multimap 是一键对多值的HashMap，类似于 Map<K, List> 的数据结构。
         */
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("name", "Jack");
        multimap.put("name", "Jack");
        multimap.put("name", "Tom");
        multimap.put("age", "10");
        multimap.put("age", "12");
        System.out.println(multimap);
        Collection<String> name = multimap.get("name");
        System.out.println(name);
        System.out.println(name.size());
    }
}
