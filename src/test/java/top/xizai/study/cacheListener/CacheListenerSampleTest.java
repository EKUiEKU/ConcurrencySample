package top.xizai.study.cacheListener;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.*;
import top.xizai.study.redisson.RedissonConstant;
import top.xizai.study.redisson.RedissonInstance;
import top.xizai.study.redisson.listener.CacheListenerSample;

import java.util.concurrent.TimeUnit;

/**
 * @author: WSC
 * @DATE: 2023/1/12
 * @DESCRIBE:
 **/
@Log4j2
public class CacheListenerSampleTest {
    public static void main(String[] args) {
        LocalCachedMapOptions<Object, Object> cachedMapOptions = LocalCachedMapOptions.defaults()
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LRU)
                .cacheSize(1000)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                .timeToLive(10000)
                .maxIdle(10, TimeUnit.SECONDS);

        RedissonClient client = RedissonInstance.getRedissonClient(args);
        RMapCache<String, String> cacheMap = client.getMapCache(RedissonConstant.REDISSON_CACHE_MAP);
        cacheMap.addListener((EntryCreatedListener<String, String>) entryEvent -> {
            String key = entryEvent.getKey();
            String value = entryEvent.getValue();
            log.info("created cache，key：{}，value：{}", key, value);
        });

        cacheMap.addListener((EntryUpdatedListener<String, String>) entryEvent -> {
            String key = entryEvent.getKey();
            String value = entryEvent.getValue();
            log.info("updated cache，key：{}，value：{}", key, value);
        });

        cacheMap.addListener((EntryExpiredListener<String, String>) entryEvent -> {
            String key = entryEvent.getKey();
            String value = entryEvent.getValue();
            log.info("expired cache，key：{}，value：{}", key, value);
        });


        cacheMap.addListener((EntryRemovedListener<String, String>) entryEvent -> {
            String key = entryEvent.getKey();
            String value = entryEvent.getValue();
            log.info("removed cache，key：{}，value：{}", key, value);
        });

        cacheMap.put("111",  "222");

        cacheMap.remove("111");

        cacheMap.put("111",  "222", 1, TimeUnit.SECONDS);

        cacheMap.put("111",  "333", 1, TimeUnit.SECONDS);
    }
}
