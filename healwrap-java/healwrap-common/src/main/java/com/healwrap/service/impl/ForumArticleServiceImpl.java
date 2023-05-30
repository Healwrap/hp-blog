package com.healwrap.service.impl;

import com.healwrap.config.AppConfig;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.FileUploadDto;
import com.healwrap.entity.dto.sysDto.SysSetting4AuditDto;
import com.healwrap.entity.enums.PageSize;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.entity.enums.UserIntegralChangeTypeEnum;
import com.healwrap.entity.enums.UserIntegralOperTypeEnum;
import com.healwrap.entity.enums.article.ArticleStatusEnum;
import com.healwrap.entity.enums.article.UpdateArticleCountTypeEnum;
import com.healwrap.entity.enums.attachment.ArticleAttachmentTypeEnum;
import com.healwrap.entity.enums.attachment.AttachmentFileTypeEnum;
import com.healwrap.entity.enums.file.FileUploadTypeEnum;
import com.healwrap.entity.enums.message.MessageStatusEnum;
import com.healwrap.entity.enums.message.MessageTypeEnum;
import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.ForumArticleAttachment;
import com.healwrap.entity.po.ForumBoard;
import com.healwrap.entity.po.UserMessage;
import com.healwrap.entity.query.ForumArticleAttachmentQuery;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.query.SimplePage;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.mappers.ForumArticleAttachmentMapper;
import com.healwrap.mappers.ForumArticleMapper;
import com.healwrap.service.ForumArticleService;
import com.healwrap.service.ForumBoardService;
import com.healwrap.service.UserInfoService;
import com.healwrap.service.UserMessageService;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.SysCacheUtils;
import com.healwrap.utils.file.FileUtils;
import com.healwrap.utils.file.ImageUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * 文章信息 业务接口实现
 */
@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

  @Resource
  private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;
  @Resource
  private ForumBoardService forumBoardService;
  @Resource
  private FileUtils fileUtils;
  @Resource
  private ForumArticleAttachmentMapper<ForumArticleAttachment, ForumArticleAttachmentQuery> forumArticleAttachmentMapper;
  @Resource
  private UserMessageService userMessageService;
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private ImageUtils imageUtils;
  @Resource
  private AppConfig appConfig;

  @Lazy
  @Resource
  private ForumArticleService forumArticleService;

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

  /**
   * 获取文章详情信息
   *
   * @param articleId
   * @return
   */
  @Override
  public ForumArticle readArticle(String articleId) {
    ForumArticle forumArticle = this.forumArticleMapper.selectByArticleId(articleId);
    if (forumArticle == null) {
      throw new BusinessException(ResponseCodeEnum.CODE_404);
    }
    if (ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())) {
      this.forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), 1, articleId);
    }
    return forumArticle;
  }

  /**
   * 发表文章
   *
   * @param isAdmin                是否是管理员
   * @param article                文章
   * @param forumArticleAttachment 文章附件
   * @param cover                  文章封面文件
   * @param attachment             文章附件文件
   */

  @Override
  public void postArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
    resetBoardInfo(isAdmin, article);
    String articleId = StringTools.getRandomString(15);
    article.setArticleId(articleId);
    Date currDate = new Date();
    article.setPostTime(currDate);
    article.setLastUpdateTime(currDate);
    if (cover == null) {
      article.setCover(appConfig.getDefaultCover());
    } else {
      FileUploadDto uploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
      article.setCover(uploadDto.getLocalPath());
    }
    if (attachment != null) {
      uploadAttachment(article, forumArticleAttachment, attachment, false);
      article.setAttachmentType(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
    } else {
      article.setAttachmentType(ArticleAttachmentTypeEnum.NO_ATTACHMENT.getType());
    }
    // 文章审核信息
    if (isAdmin) {
      article.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    } else {
      SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
      article.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.NO_AUDIT.getStatus() : ArticleStatusEnum.AUDIT.getStatus());
    }
    // 替换图片
    String content = article.getContent();
    if (!StringTools.isEmpty(content)) {
      String month = imageUtils.resetImageHtml(content);
      String replaceMonth = "/" + month + "/";
      content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
      article.setContent(content);
      String markdownContent = article.getMarkdownContent();
      if (!StringTools.isEmpty(markdownContent)) {
        markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
        article.setMarkdownContent(markdownContent);
      }
    }
    forumArticleMapper.insert(article);
    // 增加积分
    Integer postIntegral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
    if (postIntegral > 0 && ArticleStatusEnum.AUDIT.equals(article.getStatus())) {
      userInfoService.updateUserIntegral(article.getUserId(), UserIntegralOperTypeEnum.POST_ARTICLE, UserIntegralChangeTypeEnum.ADD.getChangeType(), postIntegral);
    }

  }

  @Override
  public void updateArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
    ForumArticle dbInfo = forumArticleMapper.selectByArticleId(article.getArticleId());
    if (!isAdmin && !dbInfo.getUserId().equals(article.getUserId())) {
      throw new BusinessException("非法操作");
    }
    article.setLastUpdateTime(new Date());
    resetBoardInfo(isAdmin, article);
    if (cover != null) {
      FileUploadDto uploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
      article.setCover(uploadDto.getLocalPath());
    }
    if (attachment != null) {
      uploadAttachment(article, forumArticleAttachment, attachment, true);
      article.setAttachmentType(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
    } else {
      article.setAttachmentType(ArticleAttachmentTypeEnum.NO_ATTACHMENT.getType());
    }

    ForumArticleAttachment dbAttachment = null;
    ForumArticleAttachmentQuery forumArticleAttachmentQuery = new ForumArticleAttachmentQuery();
    forumArticleAttachmentQuery.setArticleId(article.getArticleId());
    List<ForumArticleAttachment> attachmentList = forumArticleAttachmentMapper.selectList(forumArticleAttachmentQuery);
    if (!attachmentList.isEmpty()) {
      dbAttachment = attachmentList.get(0);
    }
    if (dbAttachment != null) {
      if (article.getAttachmentType() == 0) {
        new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbAttachment.getFilePath()).delete();
        forumArticleAttachmentMapper.deleteByFileId(dbAttachment.getFileId());
      } else {
        if (!dbAttachment.getIntegral().equals(forumArticleAttachment.getArticleId())) {
          ForumArticleAttachment updateAttachment = new ForumArticleAttachment();
          updateAttachment.setIntegral(forumArticleAttachment.getIntegral());
          forumArticleAttachmentMapper.updateByFileId(updateAttachment, dbAttachment.getFileId());
        }

      }
    }
    // 文章是否需要审核
    if (isAdmin) {
      article.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    } else {
      SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
      article.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.NO_AUDIT.getStatus() : ArticleStatusEnum.AUDIT.getStatus());
    }
    // 替换图片
    String content = article.getContent();
    if (!StringTools.isEmpty(content)) {
      String month = imageUtils.resetImageHtml(content);
      String replaceMonth = "/" + month + "/";
      content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
      article.setContent(content);
      String markdownContent = article.getMarkdownContent();
      if (!StringTools.isEmpty(markdownContent)) {
        markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
        article.setMarkdownContent(markdownContent);
      }
    }
    forumArticleMapper.updateByArticleId(article, article.getArticleId());
  }

  private void resetBoardInfo(Boolean isAdmin, ForumArticle forumArticle) {
    ForumBoard pBoard = forumBoardService.getForumBoardByBoardId(forumArticle.getPBoardId());
    if (pBoard == null || pBoard.getPostType() == 0 && !isAdmin) {
      throw new BusinessException("一级板块不存在");
    }
    forumArticle.setPBoardName(pBoard.getBoardName());
    if (forumArticle.getBoardId() != null && forumArticle.getBoardId() != 0) {
      ForumBoard board = forumBoardService.getForumBoardByBoardId(forumArticle.getBoardId());
      if (board == null || board.getPostType() == 0 && !isAdmin) {
        throw new BusinessException("二级板块不存在");
      }
      forumArticle.setBoardName(board.getBoardName());
    } else {
      forumArticle.setBoardId(0);
      forumArticle.setBoardName("");
    }
  }

  private void uploadAttachment(ForumArticle article, ForumArticleAttachment attachment, MultipartFile file, Boolean isUpdate) {
    long allowSizeMb = SysCacheUtils.getSysSetting().getPostSetting().getPostAttachmentSize();
    long allowSize = allowSizeMb * Constants.FILE_SIZE_1M;
    if (file.getSize() > allowSize) {
      throw new BusinessException("附件最大只能上传" + allowSizeMb + "MB");
    }
    // 修改
    ForumArticleAttachment dbInfo = null;
    if (isUpdate) {
      ForumArticleAttachmentQuery forumArticleAttachmentQuery = new ForumArticleAttachmentQuery();
      forumArticleAttachmentQuery.setArticleId(article.getArticleId());
      List<ForumArticleAttachment> attachmentList = forumArticleAttachmentMapper.selectList(forumArticleAttachmentQuery);
      if (!attachmentList.isEmpty()) {
        dbInfo = attachmentList.get(0);
        new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbInfo.getFilePath()).delete();
      }
    }
    FileUploadDto uploadDto = fileUtils.uploadFile2Local(file, Constants.FILE_FOLDER_ATTACHMENT, FileUploadTypeEnum.ARTICLE_ATTACHMENT);
    if (dbInfo == null) {
      attachment.setFileId(StringTools.getRandomNumber(15));
      attachment.setArticleId(article.getArticleId());
      attachment.setUserId(article.getUserId());
      attachment.setFileName(uploadDto.getOriginalFileName());
      attachment.setFilePath(uploadDto.getLocalPath());
      attachment.setFileSize(file.getSize());
      attachment.setDownloadCount(0);
      attachment.setFileType(AttachmentFileTypeEnum.ZIP.getType());
      forumArticleAttachmentMapper.insert(attachment);
    } else {
      ForumArticleAttachment updateInfo = new ForumArticleAttachment();
      updateInfo.setFileName(uploadDto.getOriginalFileName());
      updateInfo.setFilePath(uploadDto.getLocalPath());
      updateInfo.setFileSize(file.getSize());
      forumArticleAttachmentMapper.updateByFileId(updateInfo, dbInfo.getFileId());
    }
  }

  @Override
  public void delArticle(String articleIds) {
    String[] articleIdArr = articleIds.split(",");
    for (String articleId : articleIdArr) {
      forumArticleService.delArticleSingle(articleId);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delArticleSingle(String articleId) {
    ForumArticle article = getForumArticleByArticleId(articleId);
    if (article == null || ArticleStatusEnum.DEL.getStatus().equals(article.getStatus())) {
      throw new BusinessException("文章不存在");
    }
    ForumArticle updateInfo = new ForumArticle();
    updateInfo.setStatus(ArticleStatusEnum.DEL.getStatus());
    forumArticleMapper.updateByArticleId(updateInfo, articleId);

    Integer integral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
    if (integral > 0 && ArticleStatusEnum.AUDIT.getStatus().equals(article.getStatus())) {
      userInfoService.updateUserIntegral(article.getUserId(), UserIntegralOperTypeEnum.DEL_ARTICLE, UserIntegralChangeTypeEnum.REDUCE.getChangeType(), integral);
    }
    // 添加消息
    UserMessage userMessage = new UserMessage();
    userMessage.setReceivedUserId(article.getUserId());
    userMessage.setMessageType(MessageTypeEnum.SYS.getType());
    userMessage.setCreateTime(new Date());
    userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
    userMessage.setMessageContent("您的文章《" + article.getTitle() + "》已被管理员删除");
    userMessageService.add(userMessage);
  }

  @Override
  public void updateBoard(String articleId, Integer pBoardId, Integer boardId) {
    ForumArticle article = new ForumArticle();
    article.setPBoardId(pBoardId);
    article.setBoardId(boardId);
    resetBoardInfo(true, article);
    forumArticleMapper.updateByArticleId(article, articleId);
  }

  @Override
  public void auditArticle(String articleIds) {
    String[] articleIdArr = articleIds.split(",");
    for (String articleId : articleIdArr) {
      forumArticleService.auditArticleSingle(articleId);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void auditArticleSingle(String articleId) {
    ForumArticle article = getForumArticleByArticleId(articleId);
    if (article == null || ArticleStatusEnum.DEL.getStatus().equals(article.getStatus()) || ArticleStatusEnum.AUDIT.getStatus().equals(article.getStatus())) {
      throw new BusinessException("文章不存在或已审核");
    }
    ForumArticle updateInfo = new ForumArticle();
    updateInfo.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    forumArticleMapper.updateByArticleId(updateInfo, articleId);
    Integer integral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
    if (integral > 0 && ArticleStatusEnum.AUDIT.getStatus().equals(article.getStatus())) {
      userInfoService.updateUserIntegral(article.getUserId(), UserIntegralOperTypeEnum.POST_ARTICLE, UserIntegralChangeTypeEnum.ADD.getChangeType(), integral);
    }
  }

}
