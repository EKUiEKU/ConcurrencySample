package top.xizai.study.lock;

import com.google.common.collect.EvictingQueue;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE:
 **/
@Log4j2
public class BlockQueueSample <T> {
    private final Integer QUEUE_SIZE = 10;
    private final Integer WAIT_TIME = 30;
    private final EvictingQueue<T> evictingQueue = EvictingQueue.create(QUEUE_SIZE);

    private ReentrantLock lock = new ReentrantLock();
    private Condition emptyQueue = lock.newCondition();
    private Condition fullQueue = lock.newCondition();


    public void add(T t) {
        try {
            lock.lock();

            /**
             * 管程条件一 队列满
             */
            while (evictingQueue.size() == QUEUE_SIZE) {
                log.info("add的时候队列满, 正在等待pop");
                fullQueue.await(WAIT_TIME, TimeUnit.SECONDS);
            }
            evictingQueue.add(t);

            emptyQueue.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public T pop() {
        /**
         * 管程条件而 队列空
         */
        try {
            lock.lock();

            while (evictingQueue.size() == 0) {
                log.info("pop的时候队列为空, 正在等待新的add");
                emptyQueue.await(WAIT_TIME, TimeUnit.SECONDS);
            }
            fullQueue.signalAll();
            return evictingQueue.poll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
