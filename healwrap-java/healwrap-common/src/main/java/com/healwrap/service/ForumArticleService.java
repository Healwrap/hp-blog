package com.healwrap.service;

import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.ForumArticleAttachment;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文章信息 业务接口
 */
public interface ForumArticleService {

  /**
   * 根据条件查询列表
   */
  List<ForumArticle> findListByParam(ForumArticleQuery param);

  /**
   * 根据条件查询列表
   */
  Integer findCountByParam(ForumArticleQuery param);

  /**
   * 分页查询
   */
  PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param);

  /**
   * 新增
   */
  Integer add(ForumArticle bean);

  /**
   * 批量新增
   */
  Integer addBatch(List<ForumArticle> listBean);

  /**
   * 批量新增/修改
   */
  Integer addOrUpdateBatch(List<ForumArticle> listBean);

  /**
   * 根据ArticleId查询对象
   */
  ForumArticle getForumArticleByArticleId(String articleId);


  /**
   * 根据ArticleId修改
   */
  Integer updateForumArticleByArticleId(ForumArticle bean, String articleId);


  /**
   * 根据ArticleId删除
   */
  Integer deleteForumArticleByArticleId(String articleId);

  /**
   * 获取文章详情信息
   */
  public ForumArticle readArticle(String articleId);

  /**
   * 发表文章
   */
  void postArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment);

  /**
   * 更新文章
   */
  void updateArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment);

  /**
   * 删除文章
   */
  void delArticle(String articleIds);

  /**
   * 删除单个文章
   */
  void delArticleSingle(String articleId);

  /**
   * 重置板块
   */
  void updateBoard(String articleId, Integer pBoardId, Integer boardId);
  /**
   * 审核文章
   */
  void auditArticle(String articleIds);
  /**
   * 审核单篇文章
   */
  void auditArticleSingle(String articleId);
}
