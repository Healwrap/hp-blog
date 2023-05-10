package com.easybbs.entity.enums.comment;

/**
 * @ClassName CommentStatusEnum
 * @Description TODO
 * @Date 2023/5/9 19:12
 * @author pepedd
 */
public enum CommentStatusEnum {
  DEL(-1, "已删除"),
  NO_AUDIT(0, "未审核"),
  AUDIT(1, "已审核");
  private final Integer status;
  private final String desc;

  CommentStatusEnum(Integer status, String desc) {
    this.status = status;
    this.desc = desc;
  }

  public static CommentStatusEnum getEnumByStatus(Integer status) {
    for (CommentStatusEnum commentStatusEnum : CommentStatusEnum.values()) {
      if (commentStatusEnum.getStatus().equals(status)) {
        return commentStatusEnum;
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
