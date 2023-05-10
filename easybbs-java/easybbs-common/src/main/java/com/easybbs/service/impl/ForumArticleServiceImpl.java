package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.FileUploadDto;
import com.easybbs.entity.dto.sysDto.SysSetting4AuditDto;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.UserIntegralChangeTypeEnum;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.entity.enums.article.ArticleStatusEnum;
import com.easybbs.entity.enums.article.UpdateArticleCountTypeEnum;
import com.easybbs.entity.enums.attachment.ArticleAttachmentTypeEnum;
import com.easybbs.entity.enums.attachment.AttachmentFileTypeEnum;
import com.easybbs.entity.enums.file.FileUploadTypeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleAttachmentMapper;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.ForumBoardService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.StringTools;
import com.easybbs.utils.SysCacheUtils;
import com.easybbs.utils.file.FileUtils;
import com.easybbs.utils.file.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
  private UserInfoService userInfoService;
  @Resource
  private ImageUtils imageUtils;

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
    if (cover != null) {
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

  private void resetBoardInfo(Boolean isAdmin, ForumArticle forumArticle) {
    ForumBoard pBoard = forumBoardService.getForumBoardByBoardId(forumArticle.getpBoardId());
    if (pBoard == null || pBoard.getPostType() == 0 && !isAdmin) {
      throw new BusinessException("一级板块不存在");
    }
    forumArticle.setpBoardName(pBoard.getBoardName());
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
    if (isUpdate) {

    }
    FileUploadDto uploadDto = fileUtils.uploadFile2Local(file, Constants.FILE_FOLDER_ATTACHMENT, FileUploadTypeEnum.ARTICLE_ATTACHMENT);
    attachment.setFileId(StringTools.getRandomNumber(15));
    attachment.setArticleId(article.getArticleId());
    attachment.setUserId(article.getUserId());
    attachment.setFileName(uploadDto.getOriginalFileName());
    attachment.setFilePath(uploadDto.getLocalPath());
    attachment.setFileSize(file.getSize());
    attachment.setDownloadCount(0);
    attachment.setFileType(AttachmentFileTypeEnum.ZIP.getType());
    forumArticleAttachmentMapper.insert(attachment);
  }
}
