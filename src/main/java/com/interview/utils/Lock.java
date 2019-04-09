package com.interview.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁
 */
@Configuration
@Slf4j
public class Lock {

    private CuratorFramework client = null;

    private static final String ZK_LOCK = "pk-zk-lock";

    private static final String LOCK = "pk-lock";

    public Lock() {
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5)
                ).namespace("zk-namespace").build();

        client.start();
    }

    //获得分布式锁
    public void getLock() {


    }

    //释放分布式锁:订单创建成功或者异常的时候释放锁
    public boolean releaseLock() {
        return true;
    }
}
