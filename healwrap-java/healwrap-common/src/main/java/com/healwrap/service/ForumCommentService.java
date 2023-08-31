package com.healwrap.service;

import com.healwrap.entity.po.ForumComment;
import com.healwrap.entity.query.ForumCommentQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 评论 业务接口
 */
public interface ForumCommentService {

  /**
   * 根据条件查询列表
   */
  List<ForumComment> findListByParam(ForumCommentQuery param);

  /**
   * 根据条件查询列表
   */
  Integer findCountByParam(ForumCommentQuery param);

  /**
   * 分页查询
   */
  PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery param);

  /**
   * 分页查询，线性结构
   */
  PaginationResultVO<ForumComment> findListByPageLinear(ForumCommentQuery param);

  /**
   * 新增
   */
  Integer add(ForumComment bean);

  /**
   * 批量新增
   */
  Integer addBatch(List<ForumComment> listBean);

  /**
   * 批量新增/修改
   */
  Integer addOrUpdateBatch(List<ForumComment> listBean);

  /**
   * 根据CommentId查询对象
   */
  ForumComment getForumCommentByCommentId(Integer commentId);


  /**
   * 根据CommentId修改
   */
  Integer updateForumCommentByCommentId(ForumComment bean, Integer commentId);


  /**
   * 根据CommentId删除
   */
  Integer deleteForumCommentByCommentId(Integer commentId);

  /**
   * 评论置顶
   */
  void changeTopType(String userId, Integer commentId, Integer topType);

  /**
   * 发布评论
   */
  void postComment(ForumComment comment, MultipartFile image);

  void delComment(String commentIds);

  void delCommentSingle(String commentId);

  void auditComment(String commentIds);

  void auditCommentSingle(String commentId);
}
