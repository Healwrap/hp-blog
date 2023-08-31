package com.healwrap.entity.enums;

/**
 * @author pepedd
 * @ClassName EditorTypeEnum
 * @Description TODO
 * @Date 2023/5/10 8:51
 */
public enum EditorTypeEnum {
  RICH(0, "富文本"),
  MARKDOWN(1, "Markdown");
  private final Integer type;
  private final String desc;

  EditorTypeEnum(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public static EditorTypeEnum getByType(Integer type) {
    for (EditorTypeEnum item : EditorTypeEnum.values()) {
      if (item.getType().equals(type)) {
        return item;
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
