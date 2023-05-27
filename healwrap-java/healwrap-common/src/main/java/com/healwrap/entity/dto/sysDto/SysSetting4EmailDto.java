package com.healwrap.entity.dto.sysDto;

import lombok.Data;

/**
 * @ClassName SysSetting4EmailDto
 * @Description 邮件设置
 * @Date 2023/4/12 10:29
 * @author pepedd
 */
@Data
public class SysSetting4EmailDto {
  /**
   * 邮件标题
   */
  private String emailTitle;
  /**
   * 邮件内容
   */
  private String emailContent;
}
