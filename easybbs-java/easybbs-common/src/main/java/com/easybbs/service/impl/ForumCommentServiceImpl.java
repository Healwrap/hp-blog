package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.FileUploadDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.enums.article.ArticleStatusEnum;
import com.easybbs.entity.enums.article.UpdateArticleCountTypeEnum;
import com.easybbs.entity.enums.comment.CommentStatusEnum;
import com.easybbs.entity.enums.comment.CommentTopTypeEnum;
import com.easybbs.entity.enums.file.FileUploadTypeEnum;
import com.easybbs.entity.enums.message.MessageStatusEnum;
import com.easybbs.entity.enums.message.MessageTypeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.mappers.ForumCommentMapper;
import com.easybbs.service.ForumCommentService;
import com.easybbs.service.UserInfoService;
import com.easybbs.service.UserMessageService;
import com.easybbs.utils.StringTools;
import com.easybbs.utils.SysCacheUtils;
import com.easybbs.utils.file.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
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
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private UserMessageService userMessageService;
  @Resource
  private FileUtils fileUtils;

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
   *
   * @param userId    用户id
   * @param commentId 评论id
   * @param topType   置顶状态
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void changeTopType(String userId, Integer commentId, Integer topType) {
    CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
    // 判断是否传入了置顶状态
    if (null == topTypeEnum) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    ForumComment forumComment = forumCommentMapper.selectByCommentId(commentId);
    // 判断评论是否存在
    if (null == forumComment) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
    // 判断文章是否存在
    if (null == forumArticle) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    // 判断是否是自己的文章 或者 评论是否是一级评论
    if (!forumArticle.getUserId().equals(userId) || forumComment.getpCommentId() != 0) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    // 判断是否已经是置顶状态
    if (forumComment.getTopType().equals(topType)) {
      return;
    }
    // 判断是否是取消置顶
    if (CommentTopTypeEnum.TOP.getType().equals(topType)) {
      forumCommentMapper.updateTopTypeByArticleId(forumArticle.getArticleId());
    }
    ForumComment updateInfo = new ForumComment();
    updateInfo.setTopType(topType);
    forumCommentMapper.updateByCommentId(updateInfo, commentId);
  }

  /**
   * 发布评论
   *
   * @param comment 评论信息
   * @param image   图片
   */
  @Override
  public void postComment(ForumComment comment, MultipartFile image) {
    // 判断文章是否存在
    ForumArticle article = forumArticleMapper.selectByArticleId(comment.getArticleId());
    if (article == null || ArticleStatusEnum.NO_AUDIT.getStatus().equals(article.getStatus())) {
      throw new BusinessException("评论文章不存在");
    }
    // 判断回复的评论是否存在
    ForumComment pComment = null;
    if (comment.getpCommentId() != 0) {
      pComment = forumCommentMapper.selectByCommentId(comment.getpCommentId());
      if (pComment == null) {
        throw new BusinessException("回复的评论不存在");
      }
    }
    // 判断回复的用户是否存在
    if (!StringTools.isEmpty(comment.getReplyNickName())) {
      UserInfo userInfo = userInfoService.getUserInfoByUserId(comment.getReplyUserId());
      if (userInfo == null) {
        throw new BusinessException("回复的用户不存在");
      }
      comment.setReplyNickName(userInfo.getNickName());
    }
    comment.setPostTime(new Date());
    // 判断是否有图片
    if (image != null) {
      FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(image, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.COMMENT_IMAGE);
      comment.setImgPath(fileUploadDto.getLocalPath());
    }
    Boolean needAudit = SysCacheUtils.getSysSetting().getAuditSetting().getCommentAudit();
    comment.setStatus(needAudit ? CommentStatusEnum.NO_AUDIT.getStatus() : CommentStatusEnum.AUDIT.getStatus());
    forumCommentMapper.insert(comment);
    if (needAudit) {
      return;
    }
    updateCommentInfo(comment, pComment, article);
  }

  public void updateCommentInfo(ForumComment comment, ForumComment pComment, ForumArticle forumArticle) {
    Integer commentIntegral = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
    if (commentIntegral > 0) {
      userInfoService.updateUserIntegral(comment.getUserId(), UserIntegralOperTypeEnum.POST_COMMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), commentIntegral);
    }
    if (comment.getpCommentId() == 0) {
      forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(), 1, comment.getArticleId());
    }
    // 记录消息
    UserMessage userMessage = new UserMessage();
    userMessage.setMessageType(MessageTypeEnum.COMMENT.getType());
    userMessage.setCreateTime(new Date());
    userMessage.setArticleId(comment.getArticleId());
    userMessage.setCommentId(comment.getCommentId());
    userMessage.setSendUserId(comment.getUserId());
    userMessage.setSendNickName(comment.getNickName());
    userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
    userMessage.setArticleTitle(forumArticle.getTitle());
    if (comment.getpCommentId() == 0) {
      userMessage.setReceivedUserId(forumArticle.getUserId());
    } else if (comment.getpCommentId() != 0 && StringTools.isEmpty(comment.getReplyUserId())) {
      userMessage.setReceivedUserId(pComment.getUserId());
    } else if (comment.getpCommentId() != 0 && !StringTools.isEmpty(comment.getReplyUserId())) {
      userMessage.setReceivedUserId(comment.getReplyUserId());
    }
    if (comment.getUserId().equals(userMessage.getReceivedUserId())) {
      userMessageService.add(userMessage);
    }
  }
}
