package com.interview.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

/**
 * zk 分布式锁:
 * 节点的维护(创建、移除、监听)
 */
@Configuration
@Slf4j
public class Lock {

    private CuratorFramework client = null;

    private static final String ZK_LOCK = "pk-zk-lock";

    private static final String LOCK = "pk-lock";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public Lock() {
        client = CuratorFrameworkFactory.builder()
                .connectString("macbookpro:2181")
                .connectionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .namespace("zk-namespace")
                .build();

        client.start();
    }

    @Bean
    public CuratorFramework getClient() {
        client = client.usingNamespace("zk-namespace");

        try {
            if (client.checkExists().forPath("/" + ZK_LOCK) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE);
            }

            //监听zk目录的情况
            addWatch("/" + ZK_LOCK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    private void addWatch(String path) throws Exception {

        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = event.getData().getPath();

                    if (path.contains(LOCK)) {
                        //等待的过程
                        countDownLatch.countDown();
                    }
                }
            }
        });

    }


    //获得分布式锁
    public void getLock() {


        while (true) {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK + "/" + LOCK);
            } catch (Exception e) {
                e.printStackTrace();

                //获得分布式锁失败，就重置锁
                if (countDownLatch.getCount() <= 0) {
                    countDownLatch = new CountDownLatch(1);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            log.info("分布式锁获得成功.......");

            return;
        }

    }


    //释放分布式锁:订单创建成功或者异常的时候释放锁
    public boolean releaseLock() {

        try {
            //就是把临时目录删除
            if (client.checkExists().forPath("/" + ZK_LOCK + "/" + LOCK) != null) {
                client.delete().forPath("/" + ZK_LOCK + "/" + LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        log.info("分布式锁释放成功......");
        return true;
    }
}
