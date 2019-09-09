package cn.frank.jesquedemo.job;

import cn.frank.jesquedemo.bo.TaskDetail;
import cn.frank.jesquedemo.redis.RedisHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public class DemoJobUpgrade implements Callable<List<TaskDetail<String>>> {

  private String taskId;

  private List<String> sentences;

  @JsonIgnore
  private RedisHelper redis = RedisHelper.INSTANCE;

  public DemoJobUpgrade(String tid, List<String> segs) {
    taskId = tid;
    sentences = segs;
  }

  @Override
  public List<TaskDetail<String>> call() throws Exception {

    List<CompletableFuture<TaskDetail>> finalResult = new ArrayList<>();

    for (String item : sentences) {
      finalResult.add(CompletableFuture.supplyAsync(() -> {
        try {
          // 异步写消息到redis, 何时去收集、汇总
          Thread.sleep(300);
          log.info(item);
          // todo 写redis 信息，单个元素完成
          return new TaskDetail(item, true, "ok");
        } catch (InterruptedException e) {
          log.warn("Interrupt gg {}", ExceptionUtils.getStackTrace(e));
          return new TaskDetail(item, false, "fails");
        }
      }));
    }

    // todo 阻塞在这里获取汇总信息
    // 类似 ForkJoinPool or CountDownLatch
    List<TaskDetail<String>> fails = Lists.newArrayList();
    for (CompletableFuture<TaskDetail> res : finalResult) {
      fails.add(res.join());
    }
    return fails;
  }
}
