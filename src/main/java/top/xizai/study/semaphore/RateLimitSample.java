package top.xizai.study.semaphore;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE: 限流器
 **/
public class RateLimitSample <T, R>{
    private List<T> resources = null;
    private Semaphore semaphore = null;

    Function<Void, T> applyResourceFunc = null;

    public RateLimitSample(int maxRateLimit, Function<Void, T> applyResourceFunc) {
        resources = new Vector<>();
        for (int i = 0; i < maxRateLimit; i++) {
            resources.add(applyResourceFunc.apply(null));
        }
        semaphore = new Semaphore(maxRateLimit);
        this.applyResourceFunc = applyResourceFunc;
    }

    R execute(Function<T, R> func) {
        try {
            semaphore.acquire();
            T t = resources.remove(0);
            return func.apply(t);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            resources.add(applyResourceFunc.apply(null));
            semaphore.release();
        }
    }
}
