package com.easybbs.entity.enums;

/**
 * @author pepedd
 * @ClassName UserOperFrequencyTypeEnum
 * @Description TODO
 * @Date 2023/5/19 20:37
 */
public enum UserOperFrequencyTypeEnum {
  NO_CHECK(0, "不校验"),
  POST_ARTICLE(1, "发表文章"),
  POST_COMMENT(2, "发表评论"),
  DO_LIKE(3, "点赞"),
  IMAGE_UPLOAD(4, "图片上传");
  private final Integer operType;
  private final String desc;

  UserOperFrequencyTypeEnum(Integer operType, String desc) {
    this.operType = operType;
    this.desc = desc;
  }

  public Integer getOperType() {
    return operType;
  }

  public String getDesc() {
    return desc;
  }
}
