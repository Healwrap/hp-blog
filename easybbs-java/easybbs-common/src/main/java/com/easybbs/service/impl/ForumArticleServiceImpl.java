package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.UpdateArticleCountTypeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.service.ForumArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 文章信息 业务接口实现
 */
@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

  @Resource
  private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<ForumArticle> findListByParam(ForumArticleQuery param) {
    return this.forumArticleMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(ForumArticleQuery param) {
    return this.forumArticleMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<ForumArticle> list = this.findListByParam(param);
    PaginationResultVO<ForumArticle> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(ForumArticle bean) {
    return this.forumArticleMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<ForumArticle> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumArticleMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<ForumArticle> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumArticleMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据ArticleId获取对象
   */
  @Override
  public ForumArticle getForumArticleByArticleId(String articleId) {
    return this.forumArticleMapper.selectByArticleId(articleId);
  }

  /**
   * 根据ArticleId修改
   */
  @Override
  public Integer updateForumArticleByArticleId(ForumArticle bean, String articleId) {
    return this.forumArticleMapper.updateByArticleId(bean, articleId);
  }

  /**
   * 根据ArticleId删除
   */
  @Override
  public Integer deleteForumArticleByArticleId(String articleId) {
    return this.forumArticleMapper.deleteByArticleId(articleId);
  }

  @Override
  public ForumArticle readArticle(String articleId) {
    ForumArticle forumArticle = this.forumArticleMapper.selectByArticleId(articleId);
    if (forumArticle != null) {
      throw new BusinessException(ResponseCodeEnum.CODE_404);
    }
    if (ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())) {
      this.forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), Constants.ONE, articleId);
    }
    return forumArticle;
  }
}
