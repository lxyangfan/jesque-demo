package cn.frank.jesquedemo.service;

import cn.frank.jesquedemo.JesqueDemoApplicationTests;
import cn.frank.jesquedemo.bo.BatchTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

@Slf4j
public class BatchTaskServiceTest extends JesqueDemoApplicationTests {

  private BatchTaskService batchTaskService = BatchTaskService.INSTANCE;


  @Test
  public void test_addTask() {
    BatchTask task = BatchTask.builder()
        .taskId("uuid1")
        .status(1)
        .createTime(Instant.now().toEpochMilli())
        .build();

    batchTaskService.addTask(task);

    BatchTask taskFromRedis = batchTaskService.getTaskById("uuid1");
    Assert.assertNotNull("Not null", taskFromRedis);

  }

}
