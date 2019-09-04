package cn.frank.jesquedemo.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Slf4j
public class DemoJobUpgrade implements Runnable {

  private List<String> sentences;

  @JsonIgnore
  private static final ExecutorService threadPool = Executors.newCachedThreadPool();

  public DemoJobUpgrade(List<String> segs) {
    sentences = segs;
  }

  @Override
  public void run() {
    log.info("begin run DemoJobUpgrade");
    for (String item : sentences) {
      log.info("deal with: {}", item);
      threadPool.submit(() -> {
        Thread.sleep(300);
        log.info(item);
        return true;
      });
      log.info("end deal with: {}", item);
    }
    log.info("end run DemoJobUpgrade");
  }
}
