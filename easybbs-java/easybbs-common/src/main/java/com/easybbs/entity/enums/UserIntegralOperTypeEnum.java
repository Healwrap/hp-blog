package com.easybbs.entity.enums;

/**
 * @ClassName UserIntegralOperTypeEnum
 * @Description 用户积分操作类型
 * @Date 2023/4/12 15:54
 * @Created by admin
 */
public enum UserIntegralOperTypeEnum {
  REGISTER(1,"账号注册"),
  USER_DOWNLOAD_ATTACHMENT(2,"下载附件"),
  DOWNLOADED_ATTACHMENT(3,"附件被下载"),
  POST_COMMENT(4,"发表评论"),
  POST_ARTICLE(5,"发表文章"),
  ADMIN(6,"管理员操作"),
  DEL_ARTICLE(7,"删除文章"),
  DEL_COMMENT(8,"删除评论");
  private final Integer operType;
  private final String desc;
  UserIntegralOperTypeEnum(Integer operType, String desc) {
    this.operType = operType;
    this.desc = desc;
  }

  public static UserIntegralOperTypeEnum getByOperType(Integer operType) {
    for (UserIntegralOperTypeEnum userIntegralOperTypeEnum : UserIntegralOperTypeEnum.values()) {
      if (userIntegralOperTypeEnum.getOperType().equals(operType)) {
        return userIntegralOperTypeEnum;
      }
    }
    return null;
  }

  public Integer getOperType() {
    return operType;
  }

  public String getDesc() {
    return desc;
  }
}
