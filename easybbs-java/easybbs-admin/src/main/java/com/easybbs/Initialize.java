package com.easybbs;

import com.easybbs.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName Initalize
 * @Description TODO
 * @Date 2023/5/25 7:00
 */
@Component()
public class Initialize {
  private static final Logger logger = LoggerFactory.getLogger(Initialize.class);
  @Resource
  private SysSettingService sysSettingService;

  public void run(ApplicationArguments args) {
    // 刷新系统缓存
    sysSettingService.refreshCache();
    logger.info("系统初始化完成");
  }
}
