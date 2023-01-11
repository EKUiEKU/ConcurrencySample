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
    public static RedissonClient getRedissonClient(String[] args) {
        if (args.length < 2) {
            throw new IllegalCallerException("请输入Redis的账号或密码");
        }

        Config config = new Config();
        config.useSingleServer()
                .setAddress(args[0])
                .setPassword(args[1]);

        return Redisson.create(config);
    }
}
