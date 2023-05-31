package com.healwrap.entity.dto.sysDto;

import com.healwrap.entity.annotation.VerifyParams;
import lombok.Data;

/**
 * @author pepedd
 * @ClassName SysSetting4PostDto
 * @Description 帖子设置
 * @Date 2023/4/12 10:32
 */
@Data
public class SysSetting4PostDto {
  /**
   * 发帖积分
   */
  @VerifyParams(required = true)
  private Integer postIntegral;
  /**
   * 发帖数量阈值
   */
  @VerifyParams(required = true)
  private Integer postDayCountThreshold;
  /**
   * 上传图片阈值
   */
  @VerifyParams(required = true)
  private Integer postUploadImageCount;
  /**
   * 附件大小
   */
  @VerifyParams(required = true)
  private Integer postAttachmentSize;

}
