package top.xizai.study.redisson.rlo;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.condition.Conditions;
import top.xizai.study.redisson.RedissonInstance;
import top.xizai.study.utils.TimeUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: WSC
 * @DATE: 2023/1/12
 * @DESCRIBE: RLO = REDISSON LIVE OBJECT
 *             本例子实现服务的注册和发现的功能
 **/
@Log4j2
public class RloServiceSample {
    private final String SERVICE_NAME = "RLO_SAMPLE_SERVICE";
    private final String RLO_SERVICE_LOCK = "RLO_SERVICE_LOCK";

    private RedissonClient redissonClient = null;
    private RLock lock = null;

    private MyRedissonLiveObject latelyObject = null;

    private RLiveObjectService service = null;

    private String currentService = null;

    public RloServiceSample(String[] args) {
        redissonClient = RedissonInstance.getRedissonClient(args);
        lock = redissonClient.getLock(RLO_SERVICE_LOCK);
        service = redissonClient.getLiveObjectService();
        service.registerClass(MyRedissonLiveObject.class);
    }

    public MyRedissonLiveObject getLatelyObject() {
        try {
            lock.lock();

            Collection<MyRedissonLiveObject> objects = service.find(MyRedissonLiveObject.class, Conditions.eq("serviceName", SERVICE_NAME));

            if (objects.size() != 0) {
                return objects.iterator().next();
            }
        } finally {
            lock.unlock();
        }

        return null;
    }

    public void resignService(String ipAddr) {
        this.currentService = ipAddr;
        latelyObject = this.getLatelyObject();

        if (ObjectUtil.isNotEmpty(latelyObject)) {
            Set<String> services = latelyObject.getServices();
            services.add(ipAddr);
        } else {
            latelyObject = new MyRedissonLiveObject();
            latelyObject.setId(HashUtil.dekHash(SERVICE_NAME) + "");
            latelyObject.setServiceName(SERVICE_NAME);

            HashSet<String> services = new HashSet<>();
            services.add(ipAddr);
            latelyObject.setServices(services);
            service.persist(latelyObject);
            latelyObject = this.getLatelyObject();
        }
    }

    public void find() {
        new Thread(() -> {
            while (true) {
                String ips = latelyObject.getServices()
                        .stream()
                        .collect(Collectors.joining(","));
                log.info("当前注册的IP地址：{}", ips);

                TimeUtil.randomSleep(5000, 6000);
            }
        }).start();
    }

    public void close() {
        Set<String> services = latelyObject.getServices();
        services.remove(currentService);
    }
}
