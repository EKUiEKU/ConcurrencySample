package top.xizai.study.completionService;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author: WSC
 * @DATE: 2023/1/7
 * @DESCRIBE:
 **/
@Log4j2
public class MallReportSampleTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        MallReportSample sample = new MallReportSample();
        sample.syncMallReportData();
    }
}
