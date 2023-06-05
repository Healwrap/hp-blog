package com.healwrap.framework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pepedd
 * @ClassName AppConfig
 * @Description 应用配置
 * @Date 2023/4/11 18:16
 */
@Getter
@Component
public class AppConfig {
  /**
   * 项目名称
   */
  @Value("${healwrap.name:}")
  private String name;
  /**
   * 项目版本
   */
  @Value("${healwrap.version:}")
  private String version;

  @Value("${project.folder:}")
  private String projectFolder;
  @Value("${project.defaultAvatar:}")
  private String defaultAvatar;
  @Value("${project.defaultCover:}")
  private String defaultCover;
  @Value("${development:}")
  private Boolean dev;
}
