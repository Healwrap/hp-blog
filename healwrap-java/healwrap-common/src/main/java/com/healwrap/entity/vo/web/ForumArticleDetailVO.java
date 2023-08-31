package com.healwrap.entity.vo.web;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pepedd
 * @ClassName ForumArticleDetailVO
 * @Description TODO
 * @Date 2023/4/27 23:01
 */
@Data
public class ForumArticleDetailVO implements Serializable {
  private ForumArticleVO forumArticleVO;
  private ForumArticleAttachmentVO forumArticleAttachmentVO;
  private Boolean havaLike = false;

}
