package top.xizai.study.completionService;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE:   电商报价查询
 *               需求, 需要向三个接口请求不同的电商报价信息,然后保存到数据库。
 *               由于数据量很大,每个接口的请求时间都很长，并且每个接口的请求时间差异很大
 *               需要并行 提高接口的响应时间
 **/
@Log4j2
public class MallReportSample {
    /**
     * 电商报价接口1, 平均接口5~6s
     * @return
     */
    private String getPriceByS1() throws InterruptedException {
       log.info("正在准备请求报价接口1...");
        int ms = RandomUtil.randomInt(5000, 6000);
        TimeUnit.MILLISECONDS.sleep(ms);
        return "电商报价接口1的返回数据";
    }

    /**
     * 电商报价接口1, 平均接口3~5s
     * @return
     */
    private String getPriceByS2() throws InterruptedException {
        log.info("正在准备请求报价接口2...");
        int ms = RandomUtil.randomInt(3000, 5000);
        TimeUnit.MILLISECONDS.sleep(ms);
        return "电商报价接口2的返回数据";
    }


    /**
     * 电商报价接口3, 平均接口4~7s
     * @return
     */
    private String getPriceByS3() throws InterruptedException {
        log.info("正在准备请求报价接口3...");
        int ms = RandomUtil.randomInt(4000, 7000);
        TimeUnit.MILLISECONDS.sleep(ms);
        return "电商报价接口3的返回数据";
    }

    private void saveDB(String data) {
        log.info("正在保存到数据库....{}", data);
    }

    public void syncMallReportData() throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = ThreadPoolInstance.getInstance();
        CompletionService<String> completionService = new ExecutorCompletionService(executor);

        completionService.submit(() -> getPriceByS1());
        completionService.submit(() -> getPriceByS2());
        completionService.submit(() -> getPriceByS3());

        /**
         * 读取阻塞队列的数据
         */
        for (int i = 0; i < 3; i++) {
            Future<String> result = completionService.take();
            saveDB(result.get());
        }
    }
}
