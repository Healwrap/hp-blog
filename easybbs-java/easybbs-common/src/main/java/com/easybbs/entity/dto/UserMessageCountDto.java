package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author pepedd
 * @ClassName UserMessageCountDto
 * @Description TODO
 * @Date 2023/5/19 14:11
 */
@Data
public class UserMessageCountDto {
  private Long total = 0L;
  public Long sys = 0L;
  public Long reply = 0L;
  private Long likePost = 0L;
  private Long likeComment = 0L;
  private Long attachmentDownload = 0L;
}
