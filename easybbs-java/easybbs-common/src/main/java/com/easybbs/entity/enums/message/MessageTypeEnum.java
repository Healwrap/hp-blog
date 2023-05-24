package com.easybbs.entity.enums.message;

/**
 * @author pepedd
 * @ClassName MessageTypeEnum
 * @Description 消息类型枚举
 * @Date 2023/4/12 14:46
 */
public enum MessageTypeEnum {
  SYS(0, "system", "系统消息"),
  COMMENT(1, "reply", "回复我的"),
  ARTICLE_LIKE(2, "articleLike", "文章被点赞"),
  COMMENT_LIKE(3, "commentLike", "评论被点赞"),
  ATTACHMENT_DOWNLOAD(4, "attachmentDownload", "下载附件");
  private final Integer type;
  private final String code;
  private final String desc;

  MessageTypeEnum(Integer type, String code, String desc) {
    this.type = type;
    this.code = code;
    this.desc = desc;
  }

  public static MessageTypeEnum getByCode(String code) {
    for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
      if (messageTypeEnum.getCode().equals(code)) {
        return messageTypeEnum;
      }
    }
    return null;
  }

  public static MessageTypeEnum getByType(Integer type) {
    for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
      if (messageTypeEnum.getType().equals(type)) {
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
