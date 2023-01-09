package top.xizai.study.guarded_suspension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * @author: WSC
 * @DATE: 2023/1/8
 * @DESCRIBE:       GuardedSuspension模式
 *                 请求同步转异步,返回结果 异步装同步
 *
 *                 现实场景: 去酒店吃饭预订了一个房间
 *                 这时候去房间吃饭 发现里面的餐盘还没有受理好
 *                 这时大堂经理来, 充当Guarded 来协调这些东西,
 *                 这个时候开始等待
 *                 经理叫服务员过来收东西(同步转异步),服务员收拾到通知大堂经理,
 *                 大堂经理通知正在等待饭局的人进房间(停止等待, 异步转同步)
 *
 *                 总结： 同步线程=>(请求异步线程, 开始等待) => (异步线程结束, 停止等待唤醒)=>同步线程
 **/
public class GuardObject<T> {
    private ReentrantLock lock = new ReentrantLock();
    /**
     * 管程 是否完成的条件
     */
    private Condition done = lock.newCondition();
    /**
     * 客人的最大忍耐时间, 超过了就开始骂街了。
     */
    private Integer TIMEOUT = 1;
    /**
     * 收保护的对象, 本例子里面的房子
     */
    private T obj;
    /**
     * 映射Key和GuardObject
     */
    private final static Map<Object, GuardObject> guardObjectMap
            = new ConcurrentHashMap<>();

    private GuardObject() {}

    public static <K, T> GuardObject<T> create(K key) {
        GuardObject<T> guardObject = new GuardObject<>();
        guardObjectMap.put(key, guardObject);
        return guardObject;
    }

    public static <K, T> void onChange(K key, T obj) {
        GuardObject guardObject = guardObjectMap.remove(key);
        if (guardObject != null) {
            guardObject.doChange(obj);
        }
    }

    /**
     * 客人调用
     * @param p
     * @return
     */
    public T get(Predicate<T> p) {
        try {
            lock.lock();

            while (!p.test(obj)) {
                done.await(TIMEOUT, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        return obj;
    }

    /**
     * 服务员调用
     * @param obj
     */
    private void doChange(T obj) {
        try {
            lock.lock();
            this.obj = obj;
            done.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public <K> void forceRelease(K key) {
        GuardObject guardObject = guardObjectMap.remove(key);
        if (guardObject != null) {
            guardObject.done.signalAll();
        }
    }
}
