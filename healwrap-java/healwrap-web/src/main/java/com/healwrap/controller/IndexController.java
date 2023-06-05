package com.healwrap.controller;

import com.healwrap.framework.config.AppConfig;
import com.healwrap.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName IndexController
 * @Description API首页
 * @Date 2023/5/18 10:42
 */
@RestController
public class IndexController {
  @Resource
  private AppConfig appConfig;

  @RequestMapping("/")
  public String index() {
    return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", appConfig.getName(), appConfig.getVersion());
  }
}
