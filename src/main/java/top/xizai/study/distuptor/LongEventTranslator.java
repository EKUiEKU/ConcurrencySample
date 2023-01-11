package top.xizai.study.distuptor;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class LongEventTranslator implements EventTranslatorOneArg<LongEvent, Long> {

    @Override
    public void translateTo(LongEvent event, long sequence, Long arg0) {
        event.setNumber(arg0);
    }
}