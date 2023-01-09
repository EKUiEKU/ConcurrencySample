package top.xizai.study.semaphore;

import cn.hutool.core.lang.UUID;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE:
 **/
@Log4j2
public class RateLimitSampleTest {
    private RateLimitSample<String, Object> rateLimitSample = new RateLimitSample<>(10, this::applyToken);

    /**
     * 申请Token 这一步一般放在外面
     * @return
     */
    private String applyToken(Void t) {
        return UUID.fastUUID().toString(true);
    }

    @Test
    public void test() throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolInstance.getInstance();

        int i = 0;
        for (; i < 100; i++) {
            executor.execute(() -> {
                Object result = rateLimitSample.execute((t) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "任务执行成功, 当前执行的token是：" + t;
                });

                log.info(result);
            });
        }

        Thread.sleep(30000);
    }
}
