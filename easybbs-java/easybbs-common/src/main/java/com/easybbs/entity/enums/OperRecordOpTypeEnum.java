package com.easybbs.entity.enums;

/**
 * @ClassName OperRecordOpTypeEnum
 * @Description TODO
 * @Date 2023/4/27 23:29
 * @Created by admin
 */
public enum OperRecordOpTypeEnum {
  ARTICLE_LIKE(0, "文章点赞"),
  COMMENT_LIKE(1, "评论点赞");
  private Integer type;
  private String desc;

  public Integer getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }

  OperRecordOpTypeEnum(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }
}
