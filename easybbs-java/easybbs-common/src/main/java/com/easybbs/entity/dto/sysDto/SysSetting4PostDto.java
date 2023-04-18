package com.easybbs.entity.dto.sysDto;

/**
 * @ClassName SysSetting4PostDto
 * @Description 帖子设置
 * @Date 2023/4/12 10:32
 * @Created by admin
 */
public class SysSetting4PostDto {
  /**
   * 发帖积分
   */
  private Integer postIntegral;
  /**
   * 发帖数量阈值
   */
  private Integer postDayCountThreshold;
  /**
   * 上传图片阈值
   */
  private Integer postUploadImageCount;
  /**
   * 附件大小
   */
  private Integer postAttachmentSize;

  public Integer getPostIntegral() {
    return postIntegral;
  }

  public void setPostIntegral(Integer postIntegral) {
    this.postIntegral = postIntegral;
  }

  public Integer getPostDayCountThreshold() {
    return postDayCountThreshold;
  }

  public void setPostDayCountThreshold(Integer postDayCountThreshold) {
    this.postDayCountThreshold = postDayCountThreshold;
  }

  public Integer getPostUploadImageCount() {
    return postUploadImageCount;
  }

  public void setPostUploadImageCount(Integer postUploadImageCount) {
    this.postUploadImageCount = postUploadImageCount;
  }

  public Integer getPostAttachmentSize() {
    return postAttachmentSize;
  }

  public void setPostAttachmentSize(Integer postAttachmentSize) {
    this.postAttachmentSize = postAttachmentSize;
  }
}
