package top.xizai.study.lock;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: WSC
 * @DATE: 2023/1/4
 * @DESCRIBE: Lock和Synchronized的区别是 Lock能有效的避免死锁问题。
 * 解决死锁的方式是解决锁的不可抢占的问题
 * <p>
 * Lock提供了三种方式：1.获取不到锁的时候阻塞可中断
 * 2.获取锁支持超时
 * 3.非阻塞的获取锁, 获取不到锁直接返回结果,不阻塞。
 **/
@Log4j2
public class LockSample {
    private Lock lock = new ReentrantLock();

    // 可中断的锁
    public void interruptibly() {
        String tName = Thread.currentThread().getName();
        try {
            log.info("{} 开始尝试获取可中断的锁", tName);
            lock.lockInterruptibly();
            log.info("{} 成功获取到锁", tName);

            log.info("{} 开始睡眠30秒", tName);
            Thread.sleep(30 * 1000);

            log.info("{} 30秒过去了,已经苏醒, 开始执行任务", tName);

            for (int i = 0; i < 10; i++) {
                log.info("{} 执行第{}次任务", tName, i);
            }
            log.info("{} 任务执行完成", tName);

        } catch (InterruptedException e) {
            log.info("{} 被中断, 停止阻塞等待", tName);
            e.printStackTrace();
        } finally {
            lock.unlock();
            log.info("{} 锁被释放", tName);
        }
    }


    /**
     * 获取锁, 获取锁有超时时间
     */
    public void lockWithTimeOut() {
        String tName = Thread.currentThread().getName();
        boolean hasLock = false;
        try {
            log.info("{} 开始尝试获取可中断的锁", tName);
            hasLock = lock.tryLock(5, TimeUnit.SECONDS);

            if (!hasLock) {
                log.info("获取锁超时, 取消任务");
                return;
            }

            log.info("{} 成功获取到锁", tName);

            log.info("{} 开始睡眠30秒", tName);
            Thread.sleep(30 * 1000);

            log.info("{} 30秒过去了,已经苏醒, 开始执行任务", tName);

            for (int i = 0; i < 10; i++) {
                log.info("{} 执行第{}次任务", tName, i);
            }
            log.info("{} 任务执行完成", tName);

        } catch (InterruptedException e) {
            log.info("{} 被中断", tName);
            e.printStackTrace();
        } finally {
            if (hasLock) {
                lock.unlock();
                log.info("{} 锁被释放", tName);
            }
        }
    }

    @Test
    public void testLockInterruptibly() throws InterruptedException {
        LockSample lockSample = new LockSample();

        Thread t1 = new Thread(() -> {
            lockSample.interruptibly();
        });

        Thread t2 = new Thread(() -> {
            lockSample.interruptibly();
        });
        log.info("线程t1开始启动");
        t1.start();

        Thread.sleep(5 * 1000);

        log.info("5s过去, 线程2开始启动");
        t2.start();

        log.info("线程2获取不到锁,正在阻塞中...睡5秒");
        Thread.sleep(5 * 1000);
        log.info("线程2中断阻塞...");
        t2.interrupt();

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void testLockTimeout() throws InterruptedException {
        LockSample lockSample = new LockSample();

        Thread t1 = new Thread(() -> {
            lockSample.lockWithTimeOut();
        });

        Thread t2 = new Thread(() -> {
            lockSample.lockWithTimeOut();
        });
        log.info("线程t1开始启动");
        t1.start();

        Thread.sleep(5 * 1000);

        log.info("5s过去, 线程2开始启动");
        t2.start();


        Thread.sleep(Integer.MAX_VALUE);
    }
}
