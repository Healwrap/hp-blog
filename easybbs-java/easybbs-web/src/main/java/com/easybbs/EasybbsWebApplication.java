package com.easybbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName EasybbsWebApplication
 * @Description 管理端启动类
 * @Date 2023/4/9 10:53
 * @Created by admin
 */
@SpringBootApplication(scanBasePackages = {"com.easybbs"})
@MapperScan(basePackages = {"com.easybbs.mappers"})
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class EasybbsWebApplication {
  public static void main(String[] args) {
    SpringApplication.run(EasybbsWebApplication.class, args);
  }
}
