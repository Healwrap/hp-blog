package com.healwrap.framework.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pepedd
 * @ClassName WebConfig
 * @Description web配置
 * @Date 2023/4/11 18:18
 */
@Getter
@Component
@EqualsAndHashCode(callSuper = true)
public class WebConfig extends AppConfig {
  @Value("${spring.mail.username:}")
  private String sendUserName;

  @Value("${admin.emails:}")
  private String adminEmails;

  @Value("${inner.api.url:}")
  private String innerApi;

  @Value("${inner.api.appKey:}")
  private String innerApiAppKey;

  @Value("${inner.api.appSecret:}")
  private String innerApiAppSecret;
}
