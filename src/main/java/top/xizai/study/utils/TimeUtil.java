package top.xizai.study.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author: WSC
 * @DATE: 2023/1/9
 * @DESCRIBE:
 **/
public class TimeUtil {
    public static void randomSleep(int minMillis, int maxMillis) {
        int sleepTime = RandomUtil.randomInt(minMillis, maxMillis);
        try {
            TimeUnit.MILLISECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
