package cn.frank.jesquedemo.worker;

import net.greghaines.jesque.worker.WorkerEventEmitter;

public interface WorkerEventListener {

  /**
   * Register a WorkerListener for the specified WorkerEvents.
   */
  void addListener(WorkerEventEmitter workerEventEmitter);
}
