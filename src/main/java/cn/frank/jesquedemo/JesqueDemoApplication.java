package cn.frank.jesquedemo;

import cn.frank.jesquedemo.job.DemoJobUpgrade;
import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.*;
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
        log.info("run DemoRunner");
        Thread thread = new Thread(workerPool);
        thread.start();

        workerPool.getWorkerEventEmitter().addListener(
                (workerEvent, worker, s, job, runner, result, throwable) -> {
                    if (runner instanceof DemoJobUpgrade) {
                        // result here
                        log.info("{}", result);
                    }
                }, WorkerEvent.JOB_SUCCESS);

//    workerPool2.run();
    }
}
