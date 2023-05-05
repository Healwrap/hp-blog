package com.easybbs.service.impl;

import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.*;
import com.easybbs.entity.query.ForumArticleAttachmentDownloadQuery;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.query.UserMessageQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleAttachmentDownloadMapper;
import com.easybbs.mappers.ForumArticleAttachmentMapper;
import com.easybbs.mappers.UserMessageMapper;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 文件信息 业务接口实现
 */
@Service("forumArticleAttachmentService")
public class ForumArticleAttachmentServiceImpl implements ForumArticleAttachmentService {

  @Resource
  private ForumArticleAttachmentMapper<ForumArticleAttachment, ForumArticleAttachmentQuery> forumArticleAttachmentMapper;
  @Resource
  private ForumArticleAttachmentDownloadMapper<ForumArticleAttachmentDownload, ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadMapper;
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<ForumArticleAttachment> findListByParam(ForumArticleAttachmentQuery param) {
    return this.forumArticleAttachmentMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(ForumArticleAttachmentQuery param) {
    return this.forumArticleAttachmentMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<ForumArticleAttachment> findListByPage(ForumArticleAttachmentQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<ForumArticleAttachment> list = this.findListByParam(param);
    PaginationResultVO<ForumArticleAttachment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(ForumArticleAttachment bean) {
    return this.forumArticleAttachmentMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<ForumArticleAttachment> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumArticleAttachmentMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<ForumArticleAttachment> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.forumArticleAttachmentMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据FileId获取对象
   */
  @Override
  public ForumArticleAttachment getForumArticleAttachmentByFileId(String fileId) {
    return this.forumArticleAttachmentMapper.selectByFileId(fileId);
  }

  /**
   * 根据FileId修改
   */
  @Override
  public Integer updateForumArticleAttachmentByFileId(ForumArticleAttachment bean, String fileId) {
    return this.forumArticleAttachmentMapper.updateByFileId(bean, fileId);
  }

  /**
   * 根据FileId删除
   */
  @Override
  public Integer deleteForumArticleAttachmentByFileId(String fileId) {
    return this.forumArticleAttachmentMapper.deleteByFileId(fileId);
  }

  /**
   * 下载附件
   * @param fileId 附件ID
   * @param sessionWebUserDto 当前登录用户
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ForumArticleAttachment downloadAttachment(String fileId, SessionWebUserDto sessionWebUserDto) {
    ForumArticleAttachment forumArticleAttachment = this.forumArticleAttachmentMapper.selectByFileId(fileId);
    if (null == forumArticleAttachment) {
      throw new BusinessException("附件不存在");
    }
    ForumArticleAttachmentDownload download = null;
    // 如果附件需要积分，判断积分是否足够
    if (forumArticleAttachment.getIntegral() > 0 && !sessionWebUserDto.getUserId().equals(forumArticleAttachment.getUserId())) {
      download = this.forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId, sessionWebUserDto.getUserId());
      if (null == download) {
        UserInfo userInfo = this.userInfoService.getUserInfoByUserId(sessionWebUserDto.getUserId());
        if (userInfo.getCurrentIntegral() - forumArticleAttachment.getIntegral() < 0) {
          throw new BusinessException("积分不足");
        }
      }
    }
    ForumArticleAttachmentDownload updateDownload = new ForumArticleAttachmentDownload();
    updateDownload.setArticleId(forumArticleAttachment.getArticleId());
    updateDownload.setFileId(fileId);
    updateDownload.setUserId(sessionWebUserDto.getUserId());
    updateDownload.setDownloadCount(1);
    this.forumArticleAttachmentDownloadMapper.insertOrUpdate(updateDownload);
    this.forumArticleAttachmentMapper.updateDownloadCount(fileId);
    // 如果是自己上传的附件，不扣除积分， 或者已经下载过了
    if (sessionWebUserDto.getUserId().equals(forumArticleAttachment.getUserId()) || null != download) {
      return forumArticleAttachment;
    }
    // 扣除积分
    userInfoService.updateUserIntegral(sessionWebUserDto.getUserId(), UserIntegralOperTypeEnum.USER_DOWNLOAD_ATTACHMENT, UserIntegralChangeTypeEnum.REDUCE.getChangeType(), forumArticleAttachment.getIntegral());
    // 给附件提供者增加积分
    userInfoService.updateUserIntegral(forumArticleAttachment.getUserId(), UserIntegralOperTypeEnum.DOWNLOADED_ATTACHMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), forumArticleAttachment.getIntegral());
    // 记录消息
    ForumArticle forumArticle = this.forumArticleService.getForumArticleByArticleId(forumArticleAttachment.getArticleId());
    UserMessage userMessage = new UserMessage();
    userMessage.setMessageType(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
    userMessage.setCreateTime(new Date());
    userMessage.setArticleId(forumArticle.getArticleId());
    userMessage.setArticleTitle(forumArticle.getTitle());
    userMessage.setCommentId(0);
    userMessage.setSendUserId(sessionWebUserDto.getUserId());
    userMessage.setSendNickName(sessionWebUserDto.getNickName());
    userMessage.setReceivedUserId(forumArticleAttachment.getUserId());
    userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
    userMessageMapper.insert(userMessage);
    return forumArticleAttachment;
  }
}
