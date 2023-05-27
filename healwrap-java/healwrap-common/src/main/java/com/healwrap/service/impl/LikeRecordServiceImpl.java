package com.healwrap.service.impl;

import com.healwrap.entity.enums.*;
import com.healwrap.entity.enums.article.UpdateArticleCountTypeEnum;
import com.healwrap.entity.enums.message.MessageStatusEnum;
import com.healwrap.entity.enums.message.MessageTypeEnum;
import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.ForumComment;
import com.healwrap.entity.po.LikeRecord;
import com.healwrap.entity.po.UserMessage;
import com.healwrap.entity.query.*;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.mappers.ForumArticleMapper;
import com.healwrap.mappers.ForumCommentMapper;
import com.healwrap.mappers.LikeRecordMapper;
import com.healwrap.mappers.UserMessageMapper;
import com.healwrap.service.LikeRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 点赞记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

  @Resource
  private LikeRecordMapper<LikeRecord, LikeRecordQuery> likeRecordMapper;
  @Resource
  private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

  @Resource
  private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;
  @Resource
  private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<LikeRecord> findListByParam(LikeRecordQuery param) {
    return this.likeRecordMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(LikeRecordQuery param) {
    return this.likeRecordMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<LikeRecord> list = this.findListByParam(param);
    PaginationResultVO<LikeRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(LikeRecord bean) {
    return this.likeRecordMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<LikeRecord> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.likeRecordMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.likeRecordMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据OpId获取对象
   */
  @Override
  public LikeRecord getLikeRecordByOpId(Integer opId) {
    return this.likeRecordMapper.selectByOpId(opId);
  }

  /**
   * 根据OpId修改
   */
  @Override
  public Integer updateLikeRecordByOpId(LikeRecord bean, Integer opId) {
    return this.likeRecordMapper.updateByOpId(bean, opId);
  }

  /**
   * 根据OpId删除
   */
  @Override
  public Integer deleteLikeRecordByOpId(Integer opId) {
    return this.likeRecordMapper.deleteByOpId(opId);
  }

  /**
   * 根据ObjectIdAndUserIdAndOpType获取对象
   */
  @Override
  public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
    return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
  }

  /**
   * 根据ObjectIdAndUserIdAndOpType修改
   */
  @Override
  public Integer updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
    return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
  }

  /**
   * 根据ObjectIdAndUserIdAndOpType删除
   */
  @Override
  public Integer deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
    return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
  }

  /**
   * 点赞 文章点赞 评论点赞
   *
   * @param objectId   文章id或者评论id
   * @param userId     用户id
   * @param nickName   用户昵称
   * @param opTypeEnum 操作类型
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void doLike(String objectId, String userId, String nickName, OperRecordOpTypeEnum opTypeEnum) {
    UserMessage userMessage = new UserMessage();
    userMessage.setCreateTime(new Date());
    switch (opTypeEnum) {
      case ARTICLE_LIKE:
        ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
        if (forumArticle == null) {
          throw new BusinessException("文章不存在");
        }
        articleLike(objectId, forumArticle, userId, opTypeEnum);
        userMessage.setArticleId(objectId);
        userMessage.setArticleTitle(forumArticle.getTitle());
        userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
        userMessage.setCommentId(0);
        userMessage.setReceivedUserId(forumArticle.getUserId());
        break;
      case COMMENT_LIKE:
        ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
        if (null == forumComment) {
          throw new BusinessException("评论不存在");
        }
        commentLike(forumComment, objectId, userId, opTypeEnum);
        forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
        userMessage.setArticleId(objectId);
        userMessage.setArticleTitle(forumArticle.getTitle());
        userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
        userMessage.setCommentId(forumComment.getCommentId());
        userMessage.setReceivedUserId(forumComment.getUserId());
        userMessage.setMessageContent(forumComment.getContent());
        break;
    }
    userMessage.setSendUserId(userId);
    userMessage.setSendNickName(nickName);
    userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
    if (!userId.equals(userMessage.getReceivedUserId())) {
      UserMessage dbInfo = userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(userMessage.getArticleId(), userMessage.getCommentId(), userMessage.getSendUserId(), userMessage.getMessageType());
      if (dbInfo == null) {
        userMessageMapper.insert(userMessage);
      }
    }
  }

  /**
   * 文章点赞
   *
   * @param objId        文章id
   * @param forumArticle 文章信息
   * @param userId       用户id
   * @param opTypeEnum   操作类型
   * @return
   */
  public void articleLike(String objId, ForumArticle forumArticle, String userId, OperRecordOpTypeEnum opTypeEnum) {
    LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objId, userId, opTypeEnum.getType());
    int changeCount = 0;
    if (record != null) {
      this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objId, userId, opTypeEnum.getType());
      changeCount = -1;
    } else {
      LikeRecord likeRecord = new LikeRecord();
      likeRecord.setObjectId(objId);
      likeRecord.setUserId(userId);
      likeRecord.setOpType(opTypeEnum.getType());
      likeRecord.setCreateTime(new Date());
      likeRecord.setAuthorUserId(forumArticle.getUserId());
      this.likeRecordMapper.insert(likeRecord);
      changeCount = 1;
    }
    forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), changeCount, objId);
  }

  /**
   * 评论点赞
   *
   * @param objId      评论id
   * @param userId     用户id
   * @param opTypeEnum 操作类型
   */
  public void commentLike(ForumComment forumComment, String objId, String userId, OperRecordOpTypeEnum opTypeEnum) {
    LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objId, userId, opTypeEnum.getType());
    int changeCount = 0;
    if (record != null) {
      this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objId, userId, opTypeEnum.getType());
      changeCount = -1;
    } else {
      LikeRecord likeRecord = new LikeRecord();
      likeRecord.setObjectId(objId);
      likeRecord.setUserId(userId);
      likeRecord.setOpType(opTypeEnum.getType());
      likeRecord.setCreateTime(new Date());
      likeRecord.setAuthorUserId(forumComment.getUserId());
      this.likeRecordMapper.insert(likeRecord);
      changeCount = 1;
    }
    this.forumCommentMapper.updateCommentGoodCount(changeCount, Integer.parseInt(objId));
  }
}
