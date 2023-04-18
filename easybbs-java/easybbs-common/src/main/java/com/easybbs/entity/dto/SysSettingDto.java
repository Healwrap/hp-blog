package com.easybbs.entity.dto;

import com.easybbs.entity.dto.sysDto.*;

/**
 * @ClassName SysSettingDto
 * @Description 系统设置
 * @Date 2023/4/12 10:37
 * @Created by admin
 */
public class SysSettingDto {
  /**
   * 邮件设置
   */
  private SysSetting4EmailDto emailSetting;
  /**
   * 评论设置
   */
  private SysSetting4CommentDto commentSetting;
  /**
   * 注册设置
   */
  private SysSetting4RegisterDto registerSetting;
  /**
   * 点赞设置
   */
  private SysSetting4LikeDto likeSetting;
  /**
   * 审核设置
   */
  private SysSetting4AuditDto auditSetting;
  /**
   * 帖子设置
   */
  private SysSetting4PostDto postSetting;

  public SysSetting4EmailDto getEmailSetting() {
    return emailSetting;
  }

  public SysSetting4CommentDto getCommentSetting() {
    return commentSetting;
  }

  public void setEmailSetting(SysSetting4EmailDto emailSetting) {
    this.emailSetting = emailSetting;
  }

  public void setCommentSetting(SysSetting4CommentDto commentSetting) {
    this.commentSetting = commentSetting;
  }

  public void setRegisterSetting(SysSetting4RegisterDto registerSetting) {
    this.registerSetting = registerSetting;
  }

  public void setLikeSetting(SysSetting4LikeDto likeSetting) {
    this.likeSetting = likeSetting;
  }

  public void setAuditSetting(SysSetting4AuditDto auditSetting) {
    this.auditSetting = auditSetting;
  }

  public void setPostSetting(SysSetting4PostDto postSetting) {
    this.postSetting = postSetting;
  }

  public SysSetting4RegisterDto getRegisterSetting() {
    return registerSetting;
  }

  public SysSetting4LikeDto getLikeSetting() {
    return likeSetting;
  }

  public SysSetting4AuditDto getAuditSetting() {
    return auditSetting;
  }

  public SysSetting4PostDto getPostSetting() {
    return postSetting;
  }
}
