package cn.frank.jesquedemo.config;

import cn.frank.jesquedemo.job.DemoJob;
import cn.frank.jesquedemo.job.DemoJobUpgrade;
import cn.frank.jesquedemo.job.SendDataJob;
import cn.frank.jesquedemo.redis.RedisHelper;
import cn.frank.jesquedemo.worker.WorkerEventListener;
import com.google.common.collect.Maps;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.WorkerPool;
import net.greghaines.jesque.worker.WorkerPoolImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class JesqueConfig {

  @Value("${redis.url}")
  private String redisUrl;

  @Value("${redis.port}")
  private Integer redisPort;

  @Value("${redis.password}")
  private String redisPassword;

  @Value("${redis.connect.timeoutInMilis}")
  private Integer redisTimeout;

  @Value("${jesque.namespace:demo}")
  private String namespace;

  @Value("${jesque.queue}")
  private String queueName;

  @Bean
  public JedisPool getRedisPool() {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(50);

    if (StringUtils.isNotBlank(redisPassword)) {
      return new JedisPool(poolConfig, redisUrl, redisPort, redisTimeout, redisPassword);
    } else {
      return new JedisPool(poolConfig, redisUrl, redisPort, redisTimeout);
    }

  }

  @Bean
  public RedisHelper createRedis() {
    return RedisHelper.INSTANCE;
  }

  /**
   * ClientPoolImpl 在于获取 redis 资源使用的是 JedisPool 方式.
   */
  @Bean
  public ClientPoolImpl getResqueue(Config config, JedisPool jedisPool) {
    return new ClientPoolImpl(config, jedisPool);
  }

  @Bean
  public Config getRedisConfig() {
    Config config = null;
    if (StringUtils.isNotBlank(redisPassword)) {
      config = new ConfigBuilder()
          .withHost(redisUrl)
          .withPassword(redisPassword)
          .withPort(redisPort)
          .withNamespace(namespace)
          .withTimeout(redisTimeout).build();
    } else {
      config = new ConfigBuilder()
          .withHost(redisUrl)
          .withPort(redisPort)
          .withNamespace(namespace)
          .withTimeout(redisTimeout).build();
    }
    return config;
  }

  @Bean
  public WorkerPool createWorkerPool(Config config, JedisPool jedisPool,
      WorkerEventListener workerEventListener) {
    Map<String, Class<?>> configRes = Maps.newHashMap();
    configRes.put("SendDataJob", SendDataJob.class);
    configRes.put("DemoJobUpgrade", DemoJobUpgrade.class);

    WorkerPool wp = new WorkerPool(() -> {
      return new WorkerPoolImpl(config, Arrays.asList(queueName), new MapBasedJobFactory(configRes),
          jedisPool);
    }, 10);
    workerEventListener.addListener(wp.getWorkerEventEmitter());
    return wp;
  }


  @Bean
  public WorkerPoolImpl createWorker(Config config, JedisPool jedisPool) {
    Map<String, Class<?>> configRes = Maps.newHashMap();
    configRes.put("DemoJob", DemoJob.class);
    configRes.put("DemoJobUpgrade", DemoJobUpgrade.class);

    return new WorkerPoolImpl(config, Arrays.asList(queueName), new MapBasedJobFactory(configRes),
        jedisPool);
  }

}
