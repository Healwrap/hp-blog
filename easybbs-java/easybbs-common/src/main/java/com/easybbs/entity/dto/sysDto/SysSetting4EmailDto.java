package com.easybbs.entity.dto.sysDto;

/**
 * @ClassName SysSetting4EmailDto
 * @Description 邮件设置
 * @Date 2023/4/12 10:29
 * @Created by admin
 */
public class SysSetting4EmailDto {
  /**
   * 邮件标题
   */
  private String emailTitle;
  /**
   * 邮件内容
   */
  private String emailContent;

  public String getEmailTitle() {
    return emailTitle;
  }

  public void setEmailTitle(String emailTitle) {
    this.emailTitle = emailTitle;
  }

  public String getEmailContent() {
    return emailContent;
  }

  public void setEmailContent(String emailContent) {
    this.emailContent = emailContent;
  }
}
