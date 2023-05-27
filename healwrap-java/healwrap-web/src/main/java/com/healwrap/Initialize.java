package com.healwrap;

import com.healwrap.service.SysSettingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName Initialize
 * @Description 初始化
 * @Date 2023/4/12 10:42
 * @author pepedd
 */

@Component
public class Initialize implements ApplicationRunner {
  @Resource
  private SysSettingService sysSettingService;
  @Override
  public void run(ApplicationArguments args) throws Exception {
    sysSettingService.refreshCache();
  }
}
