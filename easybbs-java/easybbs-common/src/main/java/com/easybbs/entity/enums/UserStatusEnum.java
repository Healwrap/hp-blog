package com.easybbs.entity.enums;

/**
 * @ClassName UserStatusEnum
 * @Description 用户状态
 * @Date 2023/4/11 18:16
 * @Created by admin
 */

public enum UserStatusEnum {
  DISABLE(0, "禁用"),
  ENABLE(1, "启用");
  private Integer status;
  private String desc;

  UserStatusEnum(Integer status, String desc) {
    this.status = status;
    this.desc = desc;
  }

  public Integer getStatus() {
    return status;
  }

  public String getDesc() {
    return desc;
  }
}
