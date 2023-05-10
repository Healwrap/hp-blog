package com.easybbs.entity.dto.sysDto;

import lombok.Data;

/**
 * @ClassName SysSetting4AuditDto
 * @Description 审核设置
 * @Date 2023/4/12 9:57
 * @author pepedd
 */
@Data
public class SysSetting4AuditDto {
  /**
   * 帖子是否需要审核
   */
  private Boolean postAudit;
  /**
   * 评论是否需要审核
   */
  private Boolean commentAudit;

}
