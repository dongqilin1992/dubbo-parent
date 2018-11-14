package com.dongqilin.service;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @description:
 * @author: dongql
 * @date: 2018/3/15 14:38
 */
public class CuratorLock {
    private static InterProcessMutex lock;
    //private static Jedis jedis;
    public static void main(String[] args) throws Exception {
        final CuratorLock curatorLock = new CuratorLock();
        for (int i = 0; i < 60; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    curatorLock.seckill();
                }
            }).start();

        }
    }
    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory
                .newClient("127.0.0.1:2181", retryPolicy);
        client.start();

        lock = new InterProcessMutex(client,
                "/mylock");
        /*JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        JedisPool pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
        jedis = pool.getResource();
        if (jedis.exists("pNum")) {
            jedis.del("pNum");
        }
        jedis.set("pNum", String.valueOf(50));*/
    }

    public static InterProcessMutex getLock() {
        return lock;
    }
   /* public static Jedis getJedis() {
        return jedis;
    }*/

    public void seckill() {
        InterProcessMutex   locks=  getLock();
        try{
            // 返回锁的value值，供释放锁时候进行判断
        locks.acquire();
        /*int prdNum = Integer.valueOf(jedis.get("pNum")); //再次取得商品缓存数目
        if (prdNum > 0) {
            jedis.decr("pNum");//商品数减1
            System.out.println("好高兴，顾客:抢到商品,剩余数量:"+jedis.get("pNum"));
        } else {
            System.out.println("悲剧了，库存为0，顾客:没有抢到商品");
        }*/
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            locks.release();
            //System.out.println("我是第" + num + "号线程，我已经释放锁");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

}


