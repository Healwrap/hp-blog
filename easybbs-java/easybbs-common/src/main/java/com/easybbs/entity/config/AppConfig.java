package com.easybbs.entity.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppConfig
 * @Description 应用配置
 * @Date 2023/4/11 18:16
 * @author pepedd
 */
@Data
@Component
public class AppConfig {
  @Value("${project.folder:}")
  private String projectFolder;
}
