package cn.frank.jesquedemo.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerPoolImpl;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWorkerFactory implements Callable<Worker> {

  private Config config;
  private JedisPool jedisPool;
  private String queueName;
  private Map<String, Class<?>> configRes;

  @Override
  public Worker call() throws Exception {

    return new WorkerPoolImpl(config, Arrays.asList(queueName), new MapBasedJobFactory(configRes),
        jedisPool);
  }
}
