package com.healwrap;

import com.healwrap.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName Initalize
 * @Description TODO
 * @Date 2023/5/25 7:00
 */
@Component()
public class Initialize implements ApplicationRunner {
  private static final Logger logger = LoggerFactory.getLogger(Initialize.class);
  @Resource
  private SysSettingService sysSettingService;

  public void run(ApplicationArguments args) {
    // 刷新系统缓存
    sysSettingService.refreshCache();
    logger.info("系统初始化完成");
  }
}
