package com.healwrap.entity.enums.article;

/**
 * @author pepedd
 * @ClassName ArticleStatusEnum
 * @Description 文章状态枚举
 * @Date 2023/4/25 7:37
 */
public enum ArticleStatusEnum {
  DEL(-1, "已删除"),
  NO_AUDIT(0, "未审核"),
  AUDIT(1, "已审核");
  private final Integer status;
  private final String desc;

  ArticleStatusEnum(Integer status, String desc) {
    this.status = status;
    this.desc = desc;
  }

  public static ArticleStatusEnum getByStatus(Integer status) {
    for (ArticleStatusEnum articleStatusEnum : ArticleStatusEnum.values()) {
      if (articleStatusEnum.getStatus().equals(status)) {
        return articleStatusEnum;
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
