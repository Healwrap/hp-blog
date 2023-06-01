package com.healwrap.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pepedd
 * @ClassName AdminConfig
 * @Description TODO
 * @Date 2023/5/25 6:50
 */
@Component("adminConfig")
@Getter
public class AdminConfig extends AppConfig {
  /**
   * 管理员账号
   */
  @Value("${admin.email:}")
  private String adminAccount;
  @Value("${admin.password:}")
  private String adminPassword;
  @Value("${inner.api.url:}")
  private String webApiUrl;
}
