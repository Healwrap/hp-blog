package com.healwrap.service.impl;

import com.healwrap.service.UserInfoService;
import com.healwrap.service.UserMessageService;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.FileUploadDto;
import com.healwrap.entity.enums.PageSize;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.entity.enums.UserIntegralChangeTypeEnum;
import com.healwrap.entity.enums.UserIntegralOperTypeEnum;
import com.healwrap.entity.enums.article.ArticleStatusEnum;
import com.healwrap.entity.enums.article.UpdateArticleCountTypeEnum;
import com.healwrap.entity.enums.comment.CommentStatusEnum;
import com.healwrap.entity.enums.comment.CommentTopTypeEnum;
import com.healwrap.entity.enums.file.FileUploadTypeEnum;
import com.healwrap.entity.enums.message.MessageTypeEnum;
import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.ForumComment;
import com.healwrap.entity.po.UserInfo;
import com.healwrap.entity.po.UserMessage;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.query.ForumCommentQuery;
import com.healwrap.entity.query.SimplePage;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.mappers.ForumArticleMapper;
import com.healwrap.mappers.ForumCommentMapper;
import com.healwrap.service.ForumCommentService;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.SysCacheUtils;
import com.healwrap.utils.file.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
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
  @Lazy
  @Resource
  private ForumCommentService forumCommentService;

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
      subQuery.setPCommentIdList(pCommentIdList);
      List<ForumComment> subCommentList = this.forumCommentMapper.selectList(subQuery);
      Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getPCommentId));
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
   * 分页查询，线性结构
   */
  public PaginationResultVO<ForumComment> findListByPageLinear(ForumCommentQuery param) {
    List<ForumComment> list = this.findListByParam(param);
    // 将评论列表转换为线性结构
    List<ForumComment> temp = new ArrayList<>();
    for (ForumComment item : list) {
      if (item.getChildren() != null && !item.getChildren().isEmpty()) {
        temp.addAll(item.getChildren());
      }
    }
    list.addAll(temp);
    // 排序
    list.sort((o1, o2) -> o2.getPostTime().compareTo(o1.getPostTime()));
    // 分页
    int total = list.size();
    int pageSize = param.getPageSize() == null ? PageSize.SIZE10.getSize() : param.getPageSize();
    SimplePage page = new SimplePage(param.getPageNo(), total, pageSize);
    param.setSimplePage(page);
    list = list.subList((page.getPageNo() - 1) * pageSize, (Math.min(page.getPageNo() * pageSize, total)));
    // 加入评论文章标题
    for (ForumComment item : list) {
      ForumArticle article = this.forumArticleMapper.selectByArticleId(item.getArticleId());
      if (article != null) {
        item.setArticleTitle(article.getTitle());
      }
    }
    PaginationResultVO<ForumComment> result = new PaginationResultVO(total, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
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
    if (!forumArticle.getUserId().equals(userId) || forumComment.getPCommentId() != 0) {
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
    if (comment.getPCommentId() != 0) {
      pComment = forumCommentMapper.selectByCommentId(comment.getPCommentId());
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
    if (comment.getPCommentId() == 0) {
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
    userMessage.setMessageContent(comment.getContent());
    userMessage.setArticleTitle(forumArticle.getTitle());
    if (comment.getPCommentId() == 0) {
      userMessage.setReceivedUserId(forumArticle.getUserId());
    } else if (comment.getPCommentId() != 0 && StringTools.isEmpty(comment.getReplyUserId())) {
      userMessage.setReceivedUserId(pComment.getUserId());
    } else if (comment.getPCommentId() != 0 && !StringTools.isEmpty(comment.getReplyUserId())) {
      userMessage.setReceivedUserId(comment.getReplyUserId());
    }
//    if (comment.getUserId().equals(userMessage.getReceivedUserId())) {
//    }
    userMessageService.add(userMessage);
  }

  @Override
  public void delComment(String commentIds) {
    String[] commentIdArr = commentIds.split(",");
    for (String commentId : commentIdArr) {
      forumCommentService.delCommentSingle(commentId);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delCommentSingle(String commentId) {
    ForumComment comment = forumCommentMapper.selectByCommentId(Integer.parseInt(commentId));
    if (null == comment || CommentStatusEnum.DEL.getStatus().equals(comment.getStatus())) {
      throw new BusinessException("评论不存在");
    }
    ForumComment updateInfo = new ForumComment();
    updateInfo.setStatus(CommentStatusEnum.DEL.getStatus());
    forumCommentMapper.updateByCommentId(updateInfo, Integer.parseInt(commentId));
    // 删除已审核的文章，更新文章数量
    if (CommentStatusEnum.AUDIT.getStatus().equals(comment.getStatus())) {
      forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(), -1, comment.getArticleId());

      Integer integral = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
      userInfoService.updateUserIntegral(comment.getUserId(), UserIntegralOperTypeEnum.DEL_COMMENT, UserIntegralChangeTypeEnum.REDUCE.getChangeType(), integral);
    }
    UserMessage userMessage = new UserMessage();
    userMessage.setReceivedUserId(comment.getUserId());
    userMessage.setMessageType(MessageTypeEnum.SYS.getType());
    userMessage.setCreateTime(new Date());
    userMessage.setMessageContent("您的评论" + comment.getContent() + "已被删除");
    userMessageService.add(userMessage);
  }

  @Override
  public void auditComment(String commentIds) {
    String[] commentIdArr = commentIds.split(",");
    for (String commentId : commentIdArr) {
      forumCommentService.auditCommentSingle(commentId);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void auditCommentSingle(String commentId) {
    ForumComment comment = forumCommentMapper.selectByCommentId(Integer.parseInt(commentId));
    if (null == comment || CommentStatusEnum.DEL.getStatus().equals(Integer.parseInt(commentId)) || CommentStatusEnum.AUDIT.getStatus().equals(comment.getStatus())) {
      throw new BusinessException("评论不存在或已审核");
    }
    ForumComment updateInfo = new ForumComment();
    updateInfo.setStatus(CommentStatusEnum.AUDIT.getStatus());
    forumCommentMapper.updateByCommentId(updateInfo, Integer.parseInt(commentId));
    // 更新文章评论数量
    ForumArticle forumArticle = forumArticleMapper.selectByArticleId(comment.getArticleId());
    ForumComment pComment = null;
    if (comment.getPCommentId() != 0 && StringTools.isEmpty(comment.getReplyUserId())) {
      pComment = forumCommentMapper.selectByCommentId(comment.getPCommentId());
    }
    updateCommentInfo(comment, pComment, forumArticle);
  }
}
