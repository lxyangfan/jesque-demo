package cn.frank.jesquedemo.service;

import cn.frank.jesquedemo.bo.BatchTask;
import cn.frank.jesquedemo.redis.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Optional;

@Slf4j
public enum BatchTaskService {

  INSTANCE;

  private RedisHelper redisHelper = RedisHelper.INSTANCE;

  private static final String TASK_NAMESPACE = "tasks:";

  public String getTasksKey() {
    return TASK_NAMESPACE;
  }

  public String getTasksKey(String taskId) {
    return TASK_NAMESPACE + taskId;
  }

  public BatchTask getTaskById(String taskId) {
    String key = getTasksKey(taskId);
    return Optional.of(redisHelper.hmget(key,  BatchTask.getDeclaredFields()))
        .map(BatchTask::buildFromList)
        .orElse(null);

  }

  public void addTask(BatchTask task) {
    try (Jedis client = redisHelper.getClient()) {
      Pipeline pipeline = client.pipelined();
      pipeline.multi();
      pipeline.sadd(getTasksKey(), task.getTaskId());
      pipeline.hmset(getTasksKey(task.getTaskId()), BatchTask.toMap(task));
      pipeline.exec();
    } catch (Throwable e) {
      log.warn("BatchTaskService addTask error {}", ExceptionUtils.getStackTrace(e));
    }
  }

  public void incrSuccess(String taskId) {
    redisHelper.hincrby(getTasksKey(taskId), "success", 1L);
  }


}
