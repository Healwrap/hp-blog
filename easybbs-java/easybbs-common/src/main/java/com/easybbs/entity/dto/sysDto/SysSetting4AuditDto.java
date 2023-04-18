package com.easybbs.entity.dto.sysDto;

/**
 * @ClassName SysSetting4AuditDto
 * @Description 审核设置
 * @Date 2023/4/12 9:57
 * @Created by admin
 */

public class SysSetting4AuditDto {
  /**
   * 帖子是否需要审核
   */
  private Boolean postAudit;
  /**
   * 评论是否需要审核
   */
  private Boolean commentAudit;

  public Boolean getPostAudit() {
    return postAudit;
  }

  public void setPostAudit(Boolean postAudit) {
    this.postAudit = postAudit;
  }

  public Boolean getCommentAudit() {
    return commentAudit;
  }

  public void setCommentAudit(Boolean commentAudit) {
    this.commentAudit = commentAudit;
  }
}
