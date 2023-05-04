package com.easybbs.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppConfig
 * @Description 应用配置
 * @Date 2023/4/11 18:16
 * @Created by admin
 */
@Component
public class AppConfig {
  @Value("${project.folder:}")
  private String projectFolder;

  public String getProjectFolder() {
    return projectFolder;
  }
}
