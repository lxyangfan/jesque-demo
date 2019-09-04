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
public class DemoJob implements Runnable {

  private List<String> sentences;

  @Override
  public void run() {
    log.info("begin run DemoJob");
    for (String item : sentences) {
      log.info(item);
    }
    log.info("end run DemoJob");
  }
}
