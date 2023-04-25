package com.easybbs.entity.enums;

/**
 * @ClassName MessageTypeEnum
 * @Description 消息类型枚举
 * @Date 2023/4/12 14:46
 * @Created by admin
 */
public enum MessageTypeEnum {
  SYS(0, "sys", "系统消息"),
  COMMENT(1, "reply", "回复我的"),
  ARTICLE_LIKE(2, "article_like", "文章被点赞"),
  COMMENT_LIKE(3, "comment_like", "评论被点赞"),
  DOWNLOAD_ATTACHMENT(4, "download_attachment", "下载附件");
  private Integer type;
  private String code;
  private String desc;

  MessageTypeEnum(Integer type, String code, String desc) {
    this.type = type;
    this.code = code;
    this.desc = desc;
  }

  public static MessageTypeEnum getByType(String code) {
    for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
      if (messageTypeEnum.getCode().equals(code)) {
        return messageTypeEnum;
      }
    }
    return null;
  }

  public Integer getType() {
    return type;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }
}
