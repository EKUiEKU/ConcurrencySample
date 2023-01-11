package top.xizai.study.vt;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: WSC
 * @DATE: 2023/1/9
 * @DESCRIBE:
 **/
@Log4j2
public class VirtualThreadTest {
    // ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @Test
    public void test() {
         // executorService.execute(() -> {
         //     String tName = Thread.currentThread().getName();
         //     log.info("线程{} 正在调度中", tName);
         // });
    }
}
