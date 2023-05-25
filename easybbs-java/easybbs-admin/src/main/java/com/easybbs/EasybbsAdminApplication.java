package com.easybbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName EasybbsWebApplication
 * @Description Web端启动类
 * @Date 2023/4/9 10:53
 * @author pepedd
 */
@SpringBootApplication(scanBasePackages = {"com.easybbs"})
@MapperScan(basePackages = {"com.easybbs.mappers"})
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class EasybbsAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(EasybbsAdminApplication.class, args);
  }
}
