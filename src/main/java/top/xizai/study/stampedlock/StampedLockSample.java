package top.xizai.study.stampedlock;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE: 计算坐标到原点的长度
 **/
@Log4j2
public class StampedLockSample {

    private AtomicInteger readLockCount = new AtomicInteger();

    private double x = .0;
    private double y = .0;

    private StampedLock stampedLock = new StampedLock();

    public void update(double x, double y) {
        long stamped = stampedLock.writeLock();
        try {
            this.x = x;
            this.y = y;
        } finally {
            stampedLock.unlockWrite(stamped);
        }
    }

    public double calculate() {
        long stamped = stampedLock.tryOptimisticRead();
        double currX = this.x;
        double currY = this.y;

        // 判断数据有没有被动过
        if (!stampedLock.validate(stamped)) {
            // 开启悲观锁
            stamped = stampedLock.readLock();
            log.error("获取读锁...........");
            try {
                currX = this.x;
                currY = this.y;
            } finally {
                stampedLock.unlockRead(stamped);
                log.error("释放读锁...........");
                int count = readLockCount.incrementAndGet();
                log.error("累计读写一共冲突了{}次", count);
            }
        }
        log.info("正在计算中... 当前坐标x：{},y：{}", currX, currY);
        return Math.sqrt(currX * currX + currY * currY);
    }
}
