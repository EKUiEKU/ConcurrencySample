package top.xizai.study.completeable;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE:
 **/
@Log4j2
public class ORSampleTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolInstance.getInstance();

        CompletableFuture<String> t1 = CompletableFuture.supplyAsync(() -> {
            int ms = RandomUtil.randomInt(1000, 5000);
            log.info("t1 {}ms", ms);
            try {
                TimeUnit.MICROSECONDS.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "线程1";
        }, executor);

        CompletableFuture<String> t2 = CompletableFuture.supplyAsync(() -> {
            int ms = RandomUtil.randomInt(1000, 5000);
            log.info("t2 {}ms", ms);
            try {
                TimeUnit.MICROSECONDS.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "线程2";
        }, executor);

        CompletableFuture<String> t3 = t1.applyToEither(t2, (result) -> result);

        log.info(t3.get());
    }
}
