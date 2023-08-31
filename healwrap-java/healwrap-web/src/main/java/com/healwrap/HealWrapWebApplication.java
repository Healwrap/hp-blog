package com.healwrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author pepedd
 * @ClassName HealWrapWebApplication
 * @Description 管理端启动类
 * @Date 2023/4/9 10:53
 */
@SpringBootApplication(scanBasePackages = {"com.healwrap.**"})
@MapperScan(basePackages = {"com.healwrap.mappers"})
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableCaching // 开启缓存
public class HealWrapWebApplication {
  public static void main(String[] args) {
    SpringApplication.run(HealWrapWebApplication.class, args);
  }
}
