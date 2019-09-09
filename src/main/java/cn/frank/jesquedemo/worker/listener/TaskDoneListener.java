package cn.frank.jesquedemo.worker.listener;

import cn.frank.jesquedemo.job.DemoJobUpgrade;
import cn.frank.jesquedemo.worker.WorkerEventListener;
import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerEventEmitter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskDoneListener implements WorkerEventListener {

  @Override
  public void addListener(WorkerEventEmitter workerEventEmitter) {
    workerEventEmitter.addListener(
        (workerEvent, worker, s, job, runner, result, throwable) -> {
          if (runner instanceof DemoJobUpgrade) {
            // result here
            log.info("{}", result);
          }
        }, WorkerEvent.JOB_SUCCESS);
  }
}
