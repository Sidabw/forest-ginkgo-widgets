package com.brew.home.client_framework_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.listen.ListenerContainer;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import java.util.concurrent.TimeUnit;

/**
 * 参考：zeus-task，[blog](https://www.jianshu.com/p/70151fc0ef5d)
 */
public class Main2 {

    private static final String CONN_STR = "192.168.3.240:2181,192.168.3.240:2182,192.168.3.240:2183";

    private static final String NAMESPACE = "sidabw";
    private static final String NODE_PARENT = "/zeus-task";
    private static final String NODE_CHILD = NODE_PARENT+"/testt";

    public static void main(String[] args) throws Exception {
        //使用curator能解决原生的zk-cli链接重连、反复注册watcher等问题

        //测试时两个线程是不行的，必须开两个进程（IDEA->RUN Configuration->allow multiple instances）
        //两个client只有一个创建节点成功（client1）
        //那么关闭client1时，client2就会收到session关闭节点被删除的通知。

        //有关connection lost的监听，是推荐配置，但是测试未覆盖到。
        testMain();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    public static void testMain() {
        //1. 创建链接
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(CONN_STR)
                //重试策略：在基础sleepTime上逐渐递增
                .retryPolicy(new ExponentialBackoffRetry(1000, 1))
                //会话超时时间
                .sessionTimeoutMs(5000)
                //链接超时时间
                .connectionTimeoutMs(5000)
                //命名空间，后续当前client对节点的操作都会在该目录下进行。多个应用使用同一个zk-server时建议配置。
                .namespace(NAMESPACE)
                //ACL
                //.authorization("digest", "name:pass".getBytes())
                .build();
        client.start();

        //2. 监听链接状态
        client.getConnectionStateListenable().addListener((curatorFramework, connectionState) -> {
            System.out.println("+++connection state change:" + connectionState.name());
            if (!connectionState.isConnected() && "LOST".equals(connectionState.name())) {
                System.out.println("+++connection is confirmed lost, stop all works");
                //... 业务代码 stop task
            }
        });

        //3. 添加子节点监听（原生的watcher只能单次使用，开发者需要自己处理多次注册。这部分工作curator给做了）
        PathChildrenCache cache = new PathChildrenCache(client, NODE_PARENT, true);
        ListenerContainer<PathChildrenCacheListener> listenable = cache.getListenable();
        listenable.addListener((client2, event) -> {
            //如果子节点被删掉了，那么就重新创建子节点并开启任务
            if (PathChildrenCacheEvent.Type.CHILD_REMOVED == event.getType()) {
                System.out.println("+++monitor the path node: " + event.getData().getPath() + " removed");
                //... 如下4. 创建子节点
                //... 业务代码 start task
            }
        });
        try {
            cache.start();
        } catch (Exception e) {
            System.out.println("+++fail on add listener: "  + e.getMessage());
        }

        System.out.println("+++prepare to create node.");
        //4. 创建子节点（session关闭时删除、父节点不存在则创建父节点）
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    //.withACL(ZooDefs.Ids.CREATOR_ALL_ACL)
                    .forPath(NODE_CHILD);
            System.out.println("+++create master node success");
        } catch (Exception e) {
            if (e.getMessage().contains("NodeExists")) {
                System.out.println("+++master node has created, can't create more once");
            } else {
                System.out.println("+++ create node fail");
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (Exception e) {
            System.out.println("+++sleep interrupted.");
        }

    }

}
