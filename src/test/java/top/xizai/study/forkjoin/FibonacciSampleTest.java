package top.xizai.study.forkjoin;

import org.junit.jupiter.api.Test;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE:
 **/
public class FibonacciSampleTest {
    @Test
    public void test() {
        FibonacciSample sample = new FibonacciSample();
        sample.startCalculate(40);
    }
}
