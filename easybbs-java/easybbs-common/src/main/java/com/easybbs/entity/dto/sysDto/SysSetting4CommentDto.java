package com.easybbs.entity.dto.sysDto;

/**
 * @ClassName SysSetting4CommitDto
 * @Description 评论设置
 * @Date 2023/4/12 10:01
 * @Created by admin
 */
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

  public Integer getCommentIntegral() {
    return commentIntegral;
  }

  public Integer getCommentDayCountThreshold() {
    return commentDayCountThreshold;
  }

  public void setCommentIntegral(Integer commentIntegral) {
    this.commentIntegral = commentIntegral;
  }

  public void setCommentDayCountThreshold(Integer commentDayCountThreshold) {
    this.commentDayCountThreshold = commentDayCountThreshold;
  }

  public void setCommentEnable(Boolean commentEnable) {
    this.commentEnable = commentEnable;
  }

  public Boolean getCommentEnable() {
    return commentEnable;
  }
}
