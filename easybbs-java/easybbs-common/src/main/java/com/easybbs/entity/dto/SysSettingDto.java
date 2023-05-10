package com.easybbs.entity.dto;

import com.easybbs.entity.dto.sysDto.*;
import lombok.Data;

/**
 * @ClassName SysSettingDto
 * @Description 系统设置
 * @Date 2023/4/12 10:37
 * @author pepedd
 */
@Data
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
}
