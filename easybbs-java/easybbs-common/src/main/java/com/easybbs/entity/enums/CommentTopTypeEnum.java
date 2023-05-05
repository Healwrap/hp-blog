package com.easybbs.entity.enums;

/**
 * @ClassName CommentTopTypeEnum
 * @Description TODO
 * @Date 2023/5/5 20:14
 * @Created by pepedd
 */
public enum CommentTopTypeEnum {
  NO_TOP(0, "未置顶"),
  TOP(1, "已置顶");
  private Integer type;
  private String desc;

  CommentTopTypeEnum(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public static CommentTopTypeEnum getByType(Integer type) {
    for (CommentTopTypeEnum value : CommentTopTypeEnum.values()) {
      if (value.getType().equals(type)) {
        return value;
      }
    }
    return null;
  }

  public Integer getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }
}
