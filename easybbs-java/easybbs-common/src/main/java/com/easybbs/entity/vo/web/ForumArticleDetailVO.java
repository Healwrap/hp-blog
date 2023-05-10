package com.easybbs.entity.vo.web;

import lombok.Data;

/**
 * @ClassName ForumArticleDetailVO
 * @Description TODO
 * @Date 2023/4/27 23:01
 * @author pepedd
 */
@Data
public class ForumArticleDetailVO {
  private ForumArticleVO forumArticleVO;
  private ForumArticleAttachmentVO forumArticleAttachmentVO;
  private Boolean havaLike = false;

}
