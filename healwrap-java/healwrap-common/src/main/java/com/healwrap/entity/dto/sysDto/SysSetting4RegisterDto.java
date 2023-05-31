package com.healwrap.entity.dto.sysDto;

import com.healwrap.entity.annotation.VerifyParams;
import lombok.Data;

/**
 * @author pepedd
 * @ClassName SysSetting4RegisterDto
 * @Description 注册设置
 * @Date 2023/4/12 10:36
 */
@Data
public class SysSetting4RegisterDto {
  /**
   * 注册欢迎语
   */
  @VerifyParams(required = true)
  private String registerWelcome;
}
