package com.easybbs.service;

import java.util.List;

import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.vo.PaginationResultVO;


/**
 *
 * 文章信息 业务接口
 *
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
	Integer updateForumArticleByArticleId(ForumArticle bean,String articleId);


	/**
	 * 根据ArticleId删除
	 */
	Integer deleteForumArticleByArticleId(String articleId);
	public ForumArticle readArticle(String articleId);
}
