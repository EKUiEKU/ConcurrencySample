package top.xizai.study.distuptor.consumer;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import top.xizai.study.distuptor.LongEvent;

/**
 * 该消费者类负责将数值*20
 */
public class C22EventHandler implements EventHandler<LongEvent>, WorkHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        long number = event.getNumber();
        number *= 20;
        System.out.println(System.currentTimeMillis() + ": c2-2 consumer finished.number=" + number);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        long number = event.getNumber();
        number *= 20;
        System.out.println(System.currentTimeMillis() + ": c2-2 consumer finished.number=" + number);
    }
}