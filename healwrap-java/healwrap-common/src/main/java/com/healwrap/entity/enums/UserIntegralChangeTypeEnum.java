package com.healwrap.entity.enums;

/**
 * @author pepedd
 * @ClassName UserIntegralChangeTypeEnum
 * @Description 用户积分变更类型
 * @Date 2023/4/12 16:05
 */
public enum UserIntegralChangeTypeEnum {
  ADD(1, "增加"),
  REDUCE(-1, "减少");
  private final Integer changeType;
  private final String desc;

  UserIntegralChangeTypeEnum(Integer changeType, String desc) {
    this.changeType = changeType;
    this.desc = desc;
  }


  public Integer getChangeType() {
    return changeType;
  }

  public String getDesc() {
    return desc;
  }
}
