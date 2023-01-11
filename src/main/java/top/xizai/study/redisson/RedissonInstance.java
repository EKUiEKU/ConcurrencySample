package top.xizai.study.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class RedissonInstance {
    public static RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("")
                .setPassword("");

        return Redisson.create(config);
    }
}
