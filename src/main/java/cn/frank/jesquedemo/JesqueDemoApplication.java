package cn.frank.jesquedemo;

import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.worker.WorkerPool;
import net.greghaines.jesque.worker.WorkerPoolImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class JesqueDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(JesqueDemoApplication.class, args);
  }

}

@Component
@Slf4j
class DemoRunner implements CommandLineRunner {

  @Autowired
  private WorkerPoolImpl workerPool;

  @Autowired
  private WorkerPool workerPool2;

  @Override
  public void run(String... args) throws Exception {
    log.info("Init DemoRunner");
//        Thread thread = new Thread(workerPool);
//        thread.start();
    workerPool2.run();
    log.info("Init DemoRunner Done");
  }
}
