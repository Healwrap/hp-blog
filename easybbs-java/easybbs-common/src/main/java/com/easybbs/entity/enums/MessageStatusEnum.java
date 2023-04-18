package com.easybbs.entity.enums;

/**
 * @ClassName MessageStatusEnum
 * @Description TODO
 * @Date 2023/4/12 15:02
 * @Created by admin
 */
public enum MessageStatusEnum {
  NO_READ(0, "未读"),
  READ(1, "已读");
  private Integer status;
  private String desc;

  MessageStatusEnum(Integer status, String desc) {
    this.status = status;
    this.desc = desc;
  }

  public static MessageStatusEnum getByStatus(Integer status) {
    for (MessageStatusEnum messageStatusEnum : MessageStatusEnum.values()) {
      if (messageStatusEnum.getStatus().equals(status)) {
        return messageStatusEnum;
      }
    }
    return null;
  }

  public Integer getStatus() {
    return status;
  }

  public String getDesc() {
    return desc;
  }
}
