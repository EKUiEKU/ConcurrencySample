package top.xizai.study.completeable;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.concurrent.*;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE: 泡茶示例
 *             流程包含了：洗水壶1min 烧水15min 泡茶3min
 *                       洗茶壶1min 洗茶杯2min 拿茶叶 1min
 **/
@Log4j2
public class MakeTeaSample {
   class T1Task implements Callable<String> {
       FutureTask<String> t2Task;
       public T1Task(FutureTask<String> t2Task) {
           this.t2Task = t2Task;
       }

       @Override
       public String call() throws Exception {
           log.info("洗水壶...");
           TimeUnit.SECONDS.sleep(1);

           int i = RandomUtil.randomInt(0, 10);

           if (i > 5) {
               log.info("烧水...");
               TimeUnit.SECONDS.sleep(15);
           } else {
               log.info("刚好有滚烫的100度热水...");
           }

           String tea = t2Task.get();

           log.info("装茶叶：{}", tea);

           log.info("泡茶....");
           TimeUnit.SECONDS.sleep(3);
           return tea + "泡茶完成";
       }
   }

    class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("洗茶壶...");
            TimeUnit.SECONDS.sleep(1);
            log.info("洗茶杯...");
            TimeUnit.SECONDS.sleep(3);
            log.info("拿茶叶....");
            TimeUnit.SECONDS.sleep(1);
            return "乌龙茶";
        }
    }


    class T3Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            log.info("洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            int i = RandomUtil.randomInt(0, 10);

            if (i > 5) {
                log.info("烧水...");
                TimeUnit.SECONDS.sleep(15);
            } else {
                log.info("刚好有滚烫的100度热水...");
            }

            return "拿来滚烫的热水";
        }
    }

    public void makeTea() throws ExecutionException, InterruptedException {
        FutureTask<String> t2Future = new FutureTask<>(new T2Task());
        FutureTask<String> t1Future = new FutureTask<>(new T1Task(t2Future));
        ThreadPoolExecutor poolExecutor = ThreadPoolInstance.getInstance();
        poolExecutor.submit(t1Future);
        poolExecutor.submit(t2Future);

        log.info(t1Future.get());
    }

    public void makeTeaWithCompletableFuture() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolInstance.getInstance();
        CompletableFuture<String> boilWater = CompletableFuture.supplyAsync(() -> {
            try {
                return new T3Task().call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);

        CompletableFuture<String> washTeaCup = CompletableFuture.supplyAsync(() -> {
            try {
                return new T2Task().call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);

        CompletableFuture<String> result = boilWater.thenCombine(washTeaCup, (t, r) -> {
            log.info(t);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return r + "泡茶完成!";
        });

        log.info(result.get());
    }
}
