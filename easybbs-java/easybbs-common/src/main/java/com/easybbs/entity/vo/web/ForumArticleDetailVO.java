package com.easybbs.entity.vo.web;

/**
 * @ClassName ForumArticleDetailVO
 * @Description TODO
 * @Date 2023/4/27 23:01
 * @Created by admin
 */
public class ForumArticleDetailVO {
  private ForumArticleVO forumArticleVO;
  private ForumArticleAttachmentVO forumArticleAttachmentVO;
  private Boolean havaLike = false;

  public ForumArticleVO getForumArticleVO() {
    return forumArticleVO;
  }

  public void setForumArticleVO(ForumArticleVO forumArticleVO) {
    this.forumArticleVO = forumArticleVO;
  }

  public ForumArticleAttachmentVO getForumArticleAttachmentVO() {
    return forumArticleAttachmentVO;
  }

  public void setForumArticleAttachmentVO(ForumArticleAttachmentVO forumArticleAttachmentVO) {
    this.forumArticleAttachmentVO = forumArticleAttachmentVO;
  }

  public Boolean getHavaLike() {
    return havaLike;
  }

  public void setHavaLike(Boolean havaLike) {
    this.havaLike = havaLike;
  }
}
