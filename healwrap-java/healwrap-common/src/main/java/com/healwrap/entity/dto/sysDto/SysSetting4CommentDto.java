package com.healwrap.entity.dto.sysDto;

import lombok.Data;

/**
 * @ClassName SysSetting4CommitDto
 * @Description 评论设置
 * @Date 2023/4/12 10:01
 * @author pepedd
 */
@Data
public class SysSetting4CommentDto {
  /**
   * 评论积分
   */
  private Integer commentIntegral;
  /**
   * 评论数量阈值
   */
  private Integer commentDayCountThreshold;
  /**
   * 开启评论
   */
  private Boolean commentEnable;
}
