package cn.frank.jesquedemo.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SendDataJob implements Runnable {

  private String sentences;

  @Override
  public void run() {
    log.info("begin run SendDataJob");
    try {
      Thread.sleep(500);
      log.info("sending ====> {}", sentences);
    } catch (InterruptedException e) {

    }
    log.info("end run SendDataJob");
  }
}
