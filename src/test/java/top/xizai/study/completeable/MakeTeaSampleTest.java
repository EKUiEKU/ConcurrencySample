package top.xizai.study.completeable;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE:
 **/
public class MakeTeaSampleTest {
    MakeTeaSample makeTeaSample = new MakeTeaSample();

    @Test
    public void test() throws ExecutionException, InterruptedException {
        makeTeaSample.makeTea();
    }
    @Test
    public void testWithCompletableFuture() throws ExecutionException, InterruptedException {
        makeTeaSample.makeTeaWithCompletableFuture();
    }
}
