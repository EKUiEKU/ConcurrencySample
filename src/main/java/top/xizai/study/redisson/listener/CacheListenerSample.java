package top.xizai.study.redisson.listener;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.map.event.*;

/**
 * @author: WSC
 * @DATE: 2023/1/12
 * @DESCRIBE:
 **/
@Log4j2
public class CacheListenerSample implements EntryCreatedListener<String, String>, EntryUpdatedListener<String, String>, EntryExpiredListener<String, String>, EntryRemovedListener<String, String> {

    @Override
    public void onCreated(EntryEvent<String, String> entryEvent) {
        String key = entryEvent.getKey();
        String value = entryEvent.getValue();
        log.info("created cache，key：{}，value：{}", key, value);
    }

    @Override
    public void onExpired(EntryEvent<String, String> entryEvent) {
        String key = entryEvent.getKey();
        String value = entryEvent.getValue();
        log.info("expired cache，key：{}，value：{}", key, value);
    }

    @Override
    public void onRemoved(EntryEvent<String, String> entryEvent) {
        String key = entryEvent.getKey();
        String value = entryEvent.getValue();
        log.info("removed cache，key：{}，value：{}", key, value);
    }

    @Override
    public void onUpdated(EntryEvent<String, String> entryEvent) {
        String key = entryEvent.getKey();
        String value = entryEvent.getValue();
        log.info("updated cache，key：{}，value：{}", key, value);
    }
}
