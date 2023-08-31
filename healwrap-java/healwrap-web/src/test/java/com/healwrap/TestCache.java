package com.healwrap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description TODO
 * @Date 2023/8/31 20:03
 * @Author pepedd864
 */
@SpringBootTest
public class TestCache {
  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void testCache() {
    redisTemplate.opsForValue().set("name", "pepedd864");
    System.out.println(redisTemplate.opsForValue().get("name"));
  }
}
