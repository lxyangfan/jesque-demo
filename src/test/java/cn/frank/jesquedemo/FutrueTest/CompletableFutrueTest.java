package cn.frank.jesquedemo.FutrueTest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutrueTest {

  @Test
  public void test_allof() throws InterruptedException {
    // fuck i remember cf use deamon thread
    CompletableFuture<Boolean> cf1 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
        log.info("cf1 done");
        return true;
      } catch (Throwable e) {
        return false;
      }
    });
    CompletableFuture<Boolean> cf2 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
        log.info("cf2 done");
        return true;
      } catch (Throwable e) {
        return false;
      }
    });
    CompletableFuture.allOf(cf1, cf2)
        .thenAcceptAsync((res) -> {
          log.info("gg");
        }).join();
    log.info("quick done");
  }

  @Test
  public void test_allof2() throws InterruptedException {

    String[] segs = new String[]{
        "My", "name", "is", "you"
    };
    List<CompletableFuture<Boolean>> res = Lists.newArrayList();
    // fuck i remember cf use deamon thread
    for (String item : segs) {
      CompletableFuture<Boolean> cf1 = CompletableFuture.supplyAsync(() -> {
        try {
          Thread.sleep(2000);
          if (item.startsWith("i")) {
            throw new RuntimeException("i dont know why");
          }
          log.info("{} done", item);
          return true;
        } catch (InterruptedException e) {
          return false;
        }
      }).exceptionally((err) -> {
        log.warn("damn got err,{}", ExceptionUtils.getStackTrace(err));
        return false;
      });
      res.add(cf1);
    }

    for (CompletableFuture<?> cf : res) {
      cf.join(); // 阻塞着获取结果
    }
//    CompletableFuture.allOf((CompletableFuture<Boolean>[]) res.toArray())
//        .thenAcceptAsync((res2) -> {
//          log.info("gg");
//        }).join();
    log.info("quick done");
  }

}
