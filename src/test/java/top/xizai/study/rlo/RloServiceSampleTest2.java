package top.xizai.study.rlo;

import top.xizai.study.redisson.rlo.RloServiceSample;

/**
 * @author: WSC
 * @DATE: 2023/1/12
 * @DESCRIBE:
 **/
public class RloServiceSampleTest2 {
    public static void main(String[] args) {
        RloServiceSample sample = new RloServiceSample(args);
        sample.resignService("192.168.5.28");
        sample.find();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sample.close();
        }));
    }
}
