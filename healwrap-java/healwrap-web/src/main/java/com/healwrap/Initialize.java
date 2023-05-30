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
 * @ClassName Initialize
 * @Description 初始化
 * @Date 2023/4/12 10:42
 */

@Component
public class Initialize implements ApplicationRunner {
  private static final Logger logger = LoggerFactory.getLogger(Initialize.class);
  @Resource
  private SysSettingService sysSettingService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    sysSettingService.refreshCache();
    logger.info("系统初始化完成");
  }
}
