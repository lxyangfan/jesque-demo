package cn.frank.jesquedemo.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Slf4j
public enum RedisHelper implements ApplicationContextAware {

  INSTANCE; // singleton

  private JedisPool jedisPool;

  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
    load();
  }

  public void load() {
    if (jedisPool == null) {
      jedisPool = context.getBean(JedisPool.class); // lazy load
    }
  }

  public Jedis getClient() {
    return jedisPool.getResource();
  }

  public void hmset(String key, Map<String, String> items) {

    try (Jedis jedis = jedisPool.getResource()) {
      jedis.hmset(key, items);
    } catch (Throwable e) {
      log.warn("Redis HMSET fails. {}", ExceptionUtils.getStackTrace(e));
    }
  }

  public void hincrby(String key, String field,  long incr) {
    try (Jedis jedis = jedisPool.getResource()) {
       jedis.hincrBy(key, field, incr);
    } catch (Throwable e) {
      log.warn("Redis hincrby fails. {}", ExceptionUtils.getStackTrace(e));
    }
  }

  public List<String> hmget(String key, String... items) {

    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.hmget(key, items);
    } catch (Throwable e) {
      log.warn("Redis HMGET fails. {}", ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  public void sadd(String key, String... items) {

    try (Jedis jedis = jedisPool.getResource()) {
      jedis.sadd(key, items);
    } catch (Throwable e) {
      log.warn("Redis SADD fails. {}", ExceptionUtils.getStackTrace(e));
    }
  }

  public Long scard(String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.scard(key);
    } catch (Throwable e) {
      log.warn("Redis scard fails. {}", ExceptionUtils.getStackTrace(e));
      return -1L;
    }
  }

}
