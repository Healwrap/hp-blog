package com.easybbs.service.impl;

import com.easybbs.entity.enums.CommentTopTypeEnum;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.mappers.ForumCommentMapper;
import com.easybbs.service.ForumCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 评论 业务接口实现
 */
@Service("forumCommentService")
public class ForumCommentServiceImpl implements ForumCommentService {

  @Resource
  private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;
  @Resource
  private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<ForumComment> findListByParam(ForumCommentQuery param) {
    List<ForumComment> list = this.forumCommentMapper.selectList(param);
    // 获取二级评论
    if (param.getLoadChildren() != null && param.getLoadChildren()) {
      ForumCommentQuery subQuery = new ForumCommentQuery();
      subQuery.setQueryLikeType(param.getQueryLikeType());
      subQuery.setCurrentUserId(param.getCurrentUserId());
      subQuery.setArticleId(param.getArticleId());
      subQuery.setStatus(param.getStatus());
      List<Integer> pCommentIdList = list.stream().map(ForumComment::getCommentId).distinct().collect(Collectors.toList());
      subQuery.setpCommentIdList(pCommentIdList);
      List<ForumComment> subCommentList = this.forumCommentMapper.selectList(subQuery);
      Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getpCommentId));
      list.forEach(item -> {
        item.setChildren(tempMap.get(item.getCommentId()));
      });
    }
    return list;
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(ForumCommentQuery param) {
    return this.forumCommentMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<ForumComment> list = this.findListByParam(param);
    PaginationResultVO<ForumComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(ForumComment bean) {
    return this.forumCommentMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<ForumComment> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumCommentMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<ForumComment> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumCommentMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据CommentId获取对象
   */
  @Override
  public ForumComment getForumCommentByCommentId(Integer commentId) {
    return this.forumCommentMapper.selectByCommentId(commentId);
  }

  /**
   * 根据CommentId修改
   */
  @Override
  public Integer updateForumCommentByCommentId(ForumComment bean, Integer commentId) {
    return this.forumCommentMapper.updateByCommentId(bean, commentId);
  }

  /**
   * 根据CommentId删除
   */
  @Override
  public Integer deleteForumCommentByCommentId(Integer commentId) {
    return this.forumCommentMapper.deleteByCommentId(commentId);
  }

  /**
   * 切换置顶状态
   * @param userId 用户id
   * @param commentId 评论id
   * @param topType 置顶状态
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void changeTopType(String userId, Integer commentId, Integer topType) {
    CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
    if (null == topTypeEnum) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    ForumComment forumComment = forumCommentMapper.selectByCommentId(commentId);
    if (null == forumComment) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
    if (null == forumArticle) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    if (!forumArticle.getUserId().equals(userId) || forumComment.getpCommentId() != 0) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    if (forumComment.getTopType().equals(topType)) {
      return;
    }
    if (CommentTopTypeEnum.TOP.getType().equals(topType)) {
      forumCommentMapper.updateTopTypeByArticleId(forumArticle.getArticleId());
    }
    ForumComment updateInfo = new ForumComment();
    updateInfo.setTopType(topType);
    forumCommentMapper.updateByCommentId(updateInfo, commentId);
  }
}
