package com.easybbs.entity.enums;

import com.easybbs.entity.constants.Constants;

/**
 * @ClassName FileUploadTypeEnum
 * @Description TODO
 * @Date 2023/5/9 20:37
 * @Created by pepedd
 */
public enum FileUploadTypeEnum {
  ARTICLE_COVER("文章封面", Constants.IMAGE_SUFFIX),
  ARTICLE_ATTACHMENT("文章附件", Constants.FILE_SUFFIX),
  COMMENT_IMAGE("评论图片", Constants.IMAGE_SUFFIX),
  AVATAR("头像", Constants.IMAGE_SUFFIX);
  private final String desc;
  private final String[] suffixArray;

  FileUploadTypeEnum(String desc, String[] suffixArray) {
    this.desc = desc;
    this.suffixArray = suffixArray;
  }

  public String getDesc() {
    return desc;
  }

  public String[] getSuffixArray() {
    return suffixArray;
  }
}
