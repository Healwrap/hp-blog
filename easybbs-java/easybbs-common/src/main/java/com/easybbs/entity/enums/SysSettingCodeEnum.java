package com.easybbs.entity.enums;

import net.sf.jsqlparser.statement.Commit;

/**
 * @ClassName SysSettingCodeEnum
 * @Description 系统设置代码枚举
 * @Date 2023/4/12 13:31
 * @author pepedd
 */
public enum SysSettingCodeEnum {
  AUDIT("audit","com.easybbs.entity.dto.sysDto.SysSetting4AuditDto","auditSetting","审核设置"),
  COMMENT("comment","com.easybbs.entity.dto.sysDto.SysSetting4CommentDto","commentSetting","评论设置"),
  POST("post","com.easybbs.entity.dto.sysDto.SysSetting4PostDto","postSetting","帖子设置"),
  LIKE("like","com.easybbs.entity.dto.sysDto.SysSetting4LikeDto","likeSetting","点赞设置"),
  REGISTER("register","com.easybbs.entity.dto.sysDto.SysSetting4RegisterDto","registerSetting","注册设置"),
  EMAIL("email","com.easybbs.entity.dto.sysDto.SysSetting4EmailDto","emailSetting","邮件设置");

  private final String code;
  private final String clazz;
  private final String propName;
  private final String desc;
  SysSettingCodeEnum(String code, String clazz, String propName, String desc) {
    this.code = code;
    this.clazz = clazz;
    this.propName = propName;
    this.desc = desc;
  }

  public static SysSettingCodeEnum getByCode(String code) {
    for (SysSettingCodeEnum sysSettingCodeEnum : SysSettingCodeEnum.values()) {
      if (sysSettingCodeEnum.getCode().equals(code)) {
        return sysSettingCodeEnum;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public String getClazz() {
    return clazz;
  }

  public String getPropName() {
    return propName;
  }

  public String getDesc() {
    return desc;
  }
}
