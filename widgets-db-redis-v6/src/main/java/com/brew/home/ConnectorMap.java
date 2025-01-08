package com.brew.home;

import java.util.HashMap;

/**
 * @author shaogz
 * @since 2025/1/8 16:20
 */
public class ConnectorMap<K,V> extends HashMap<K,V> {

    public V remove(Object key) {
        V res = super.remove(key);
        //因为这个map，会以CacheAccessor的形式传递到ClientSideCaching对象中
        //而lettuce收到redis6的key evict的通知时，会调用到CacheAccessor里的map的remove方法
        //所以这里，相当于变相的传递了一个监听器给ClientSideCaching对象
        System.out.println("got a remove message.");
        return res;
    }
}
