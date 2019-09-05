package cn.frank.jesquedemo.job;

import cn.frank.jesquedemo.redis.RedisHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public class DemoJobUpgrade implements Runnable {

  private String taskId;

  private List<String> sentences;

  @JsonIgnore
  private RedisHelper redis = RedisHelper.INSTANCE;

  public DemoJobUpgrade(String tid, List<String> segs) {
    taskId = tid;
    sentences = segs;
  }

  @Override
  public void run() {
    log.info("begin run DemoJobUpgrade");

    List<CompletableFuture<?>> finalResult = new ArrayList<>();

    for (String item : sentences) {
      finalResult.add(CompletableFuture.supplyAsync(() -> {
        try {
          // 异步写消息到redis, 何时去收集、汇总
          Thread.sleep(300);
          log.info(item);
          // todo 写redis 信息，单个元素完成
          return true;
        } catch (Throwable e) {
          log.warn("somthing gg {}", ExceptionUtils.getStackTrace(e));
          return false;
        }
      }));

    }

    // todo 阻塞在这里获取汇总信息
    // 类似 ForkJoinPool or CountDownLatch
    CompletableFuture.allOf((CompletableFuture<?>[]) finalResult.toArray())
        .thenAccept((res) -> {
          // todo do the recording
          log.warn("=============== TASK {} end of line ==================== ", taskId);
        });
    log.info("end run DemoJobUpgrade");
  }
}
