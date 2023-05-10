package com.easybbs.entity.dto.sysDto;

import lombok.Data;

/**
 * @ClassName SysSetting4LikeDto
 * @Description 点赞设置
 * @Date 2023/4/12 10:31
 * @author pepedd
 */
@Data
public class SysSetting4LikeDto {
  /**
   * 点赞阈值
   */
  private Integer likeDayCountThreshold;
}
