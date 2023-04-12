package com.easybbs.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * &#064; ClassName WebConfig
 * &#064; Description TODO
 * &#064; Date 2023/4/11 18:18
 * &#064; Created by admin
 */
@Component
public class WebConfig extends AppConfig{
  @Value("${spring.mail.username:}")
  private String sendUserName;

  public String getSendUserName() {
    return sendUserName;
  }
}
