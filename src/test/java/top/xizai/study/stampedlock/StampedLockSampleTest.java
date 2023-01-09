package top.xizai.study.stampedlock;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE: 读写锁的升级版, 乐观读最好的情况可以无所
 **/
@Log4j2
public class StampedLockSampleTest {
    private StampedLockSample stampedLockSample = new StampedLockSample();

    public void doUpdatePosition() {
        new Thread(() -> {
            while (true) {
                double x = RandomUtil.randomDouble();
                double y = RandomUtil.randomDouble();
                stampedLockSample.update(x, y);
                log.info("更新坐标x: {} y：{}", x, y);
                try {
                    Thread.sleep(RandomUtil.randomInt(1000, 2000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void doCalculatePosition() {
        new Thread(() -> {
            while (true) {
                double dist = stampedLockSample.calculate();
                log.info("计算完成, 距离是：{}", dist);
                try {
                    Thread.sleep(RandomUtil.randomInt(50, 100));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Test
    public void test() {
        ThreadPoolExecutor executor = ThreadPoolInstance.getInstance();

        doUpdatePosition();

        for (int i = 0; i < 40; i++) {
            executor.execute(() -> doCalculatePosition());
        }

        while (true);
    }
}
