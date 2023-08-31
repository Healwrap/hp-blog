package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.query.ForumCommentQuery;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.ForumCommentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName ForumCommentController
 * @Description TODO
 * @Date 2023/5/30 19:14
 */
@RestController
@RequestMapping("/comment")
public class ForumCommentController extends ABaseController {
  @Resource
  private ForumCommentService forumCommentService;

  /**
   * 加载评论列表
   *
   * @param commentQuery 查询条件
   * @return ResponseVO
   */
  @PostMapping("/loadCommentList")
  @Cacheable(value = "commentList", key = "#commentQuery.pageNo")
  public ResponseVO loadCommentList(ForumCommentQuery commentQuery) {
    commentQuery.setLoadChildren(true);
    commentQuery.setOrderBy("post_time desc");
    commentQuery.setPCommentId(0);
    return getSuccessResponseVO(forumCommentService.findListByPageLinear(commentQuery));
  }

  /**
   * 删除评论
   *
   * @param commentIds 评论id数组
   * @return ResponseVO
   */
  @DeleteMapping("/deleteComment")
  @CacheEvict(value = {"commentList", "articleList"}, allEntries = true)
  @GlobalIntercepter(checkParams = true)
  public ResponseVO deleteComment(@VerifyParams(required = true) String commentIds) {
    forumCommentService.delComment(commentIds);
    return getSuccessResponseVO(null);
  }

  /**
   * 审核评论
   *
   * @param commentIds 评论id数组
   * @return ResponseVO
   */
  @PatchMapping("/auditComment")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO auditComment(@VerifyParams(required = true) String commentIds) {
    forumCommentService.auditComment(commentIds);
    return getSuccessResponseVO(null);
  }
}
