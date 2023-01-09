package top.xizai.study.forkjoin;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE: 计算算斐波那契数列
 **/
@Log4j2
public class FibonacciSample {

    class FibonacciTask extends RecursiveTask<Integer> {
        private int n;

        public FibonacciTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }

            FibonacciTask f1 = new FibonacciTask(n - 1);
            // 启动异步子任务
            f1.fork();
            FibonacciTask f2 = new FibonacciTask(n - 2);

            // f2.compute() 本线程计算
            // 计算完成之后等待子线程 f1.join()
            // 最后 + 把两个结果合并起来
            return f2.compute() + f1.join();
        }
    }

    public void startCalculate(int n) {
        ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors() + 1);
        FibonacciTask fibonacciTask = new FibonacciTask(n);
        log.info(fjp.invoke(fibonacciTask));
    }
}
