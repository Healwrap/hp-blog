package com.easybbs.entity.dto.sysDto;

/**
 * @ClassName SysSetting4LikeDto
 * @Description 点赞设置
 * @Date 2023/4/12 10:31
 * @Created by admin
 */
public class SysSetting4LikeDto {
  public Integer getLikeDayCountThreshold() {
    return likeDayCountThreshold;
  }

  public void setLikeDayCountThreshold(Integer likeDayCountThreshold) {
    this.likeDayCountThreshold = likeDayCountThreshold;
  }

  /**
   * 点赞阈值
   */
  private Integer likeDayCountThreshold;
}
