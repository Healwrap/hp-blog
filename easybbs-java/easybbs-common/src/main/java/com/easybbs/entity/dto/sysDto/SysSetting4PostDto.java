package com.easybbs.entity.dto.sysDto;

import lombok.Data;

/**
 * @ClassName SysSetting4PostDto
 * @Description 帖子设置
 * @Date 2023/4/12 10:32
 * @Created by admin
 */
@Data
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

}
