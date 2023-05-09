package com.easybbs.entity.enums;

/**
 * @ClassName UpdateArticleCountTypeEnum
 * @Description TODO
 * @Date 2023/4/27 20:50
 * @Created by admin
 */
public enum UpdateArticleCountTypeEnum {
  READ_COUNT(0, "阅读数"),
  GOOD_COUNT(1, "点赞数"),
  COMMENT_COUNT(2, "评论数");
  private final Integer type;
  private final String desc;

  public Integer getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }

  UpdateArticleCountTypeEnum(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public static UpdateArticleCountTypeEnum getByType(Integer type) {
    for (UpdateArticleCountTypeEnum updateArticleCountTypeEnum : UpdateArticleCountTypeEnum.values()) {
      if (updateArticleCountTypeEnum.getType().equals(type)) {
        return updateArticleCountTypeEnum;
      }
    }
    return null;
  }
}
