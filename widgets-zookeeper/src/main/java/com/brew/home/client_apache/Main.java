package com.brew.home.client_apache;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.AddWatchMode.PERSISTENT;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        System.out.println("Hello world!");

        ZooKeeper zk = new ZooKeeper("192.168.31.26:2181,192.168.31.26:2182,192.168.31.26:2183", 4000, (event)->{
            System.out.println("event:::::" + event);
        });

        //zk3.6版本后的特性，addWatch添加的watcher可以是永久的。
        zk.addWatch("/runoob", PERSISTENT);
        TimeUnit.SECONDS.sleep(20);
        System.out.println(zk.getState());

        //相当于用stat，并注册watcher
        //zk.exists("", (event) ->{})

        //相当于get -w /runoob
        //zk.getData("/runoob", (event) -> {}, new Stat());

//        String created = zk.create("/runoob", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(created);



    }

}