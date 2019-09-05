package cn.frank.jesquedemo.batch;

import cn.frank.jesquedemo.bo.BatchTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class BatchTaskBOTest {

  @Test
  public void test_toMap() {

    BatchTask batchTask = new BatchTask();
    Map<String, String> gg = BatchTask.toMap(batchTask);
    log.info("{}", gg);

  }
  @Test
  public void test_getFieldsList() {

    String[] gg = BatchTask.getDeclaredFields();
    log.info("{}", gg);

  }

}
