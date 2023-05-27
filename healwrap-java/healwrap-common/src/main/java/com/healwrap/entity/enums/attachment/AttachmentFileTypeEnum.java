package com.healwrap.entity.enums.attachment;

/**
 * @author pepedd
 * @ClassName AttachmentFileTypeEnum
 * @Description TODO
 * @Date 2023/5/10 10:32
 */
public enum AttachmentFileTypeEnum {
  ZIP(0, new String[]{".zip", ".rar"}, "压缩包");
  private final Integer type;
  private final String[] suffix;
  private final String desc;

  AttachmentFileTypeEnum(Integer type, String[] suffix, String desc) {
    this.type = type;
    this.suffix = suffix;
    this.desc = desc;
  }

  public static AttachmentFileTypeEnum getByType(Integer type) {
    for (AttachmentFileTypeEnum item : AttachmentFileTypeEnum.values()) {
      if (item.getType().equals(type)) {
        return item;
      }
    }
    return null;
  }

  public Integer getType() {
    return type;
  }

  public String[] getSuffix() {
    return suffix;
  }

  public String getDesc() {
    return desc;
  }
}
