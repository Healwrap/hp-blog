package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.config.WebConfig;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.SessionWebUserDto;
import com.healwrap.entity.enums.EditorTypeEnum;
import com.healwrap.entity.enums.OperRecordOpTypeEnum;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.entity.enums.UserOperFrequencyTypeEnum;
import com.healwrap.entity.enums.article.ArticleOrderTypeEnum;
import com.healwrap.entity.enums.article.ArticleStatusEnum;
import com.healwrap.entity.po.*;
import com.healwrap.entity.query.ForumArticleAttachmentQuery;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.entity.vo.web.ForumArticleAttachmentVO;
import com.healwrap.entity.vo.web.ForumArticleDetailVO;
import com.healwrap.entity.vo.web.ForumArticleVO;
import com.healwrap.entity.vo.web.UserDownloadInfoVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.service.*;
import com.healwrap.utils.CopyTools;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.html.EscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author pepedd
 * @ClassName ForumArticleController
 * @Description 论坛文章控制器
 * @Date 2023/4/24 22:39
 */
@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {
  private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);
  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private ForumArticleAttachmentService forumArticleAttachmentService;
  @Resource
  private LikeRecordService likeRecordService;
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;
  @Resource
  private WebConfig webConfig;
  @Resource
  private ForumBoardService forumBoardService;

  /**
   * 加载文章列表
   *
   * @param session   用户session
   * @param pageNo    页码
   * @param boardId   板块id
   * @param pBoardId  父板块id
   * @param orderType 排序类型
   * @return 文章列表
   */
  @GetMapping("/loadArticle")
  public ResponseVO loadArticle(HttpSession session, Integer pageNo, Integer boardId, Integer pBoardId, Integer orderType) {
    ForumArticleQuery forumArticleQuery = new ForumArticleQuery();
    forumArticleQuery.setBoardId(boardId == null || boardId == 0 ? null : boardId);
    forumArticleQuery.setPBoardId(pBoardId);
    forumArticleQuery.setPageNo(pageNo);

    SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
    if (sessionWebUserDto == null) {
      forumArticleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    }
    forumArticleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    forumArticleQuery.setCurrentUserId(sessionWebUserDto == null ? null : sessionWebUserDto.getUserId());
    ArticleOrderTypeEnum orderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
    orderTypeEnum = orderTypeEnum == null ? ArticleOrderTypeEnum.HOT : orderTypeEnum;
    forumArticleQuery.setOrderBy(orderTypeEnum.getOrderSql());
    PaginationResultVO resultVO = forumArticleService.findListByPage(forumArticleQuery);
    return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
  }

  /**
   * 获取文章详情
   *
   * @param session   用户session
   * @param articleId 文章id
   * @return 文章详情
   */

  @GetMapping("/getArticleDetail")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO getArticleDetail(HttpSession session, @VerifyParams(required = true) String articleId) {
    SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
    ForumArticle forumArticle = forumArticleService.readArticle(articleId);
    if (null == forumArticle
        || (ArticleStatusEnum.NO_AUDIT.getStatus().equals(forumArticle.getStatus())
        && (sessionWebUserDto == null || !sessionWebUserDto.getUserId().equals(forumArticle.getUserId()) || !sessionWebUserDto.getIsAdmin()))
        || ArticleStatusEnum.DEL.getStatus().equals(forumArticle.getStatus())) {
      throw new BusinessException(ResponseCodeEnum.CODE_404);
    }
    ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
    detailVO.setForumArticleVO(CopyTools.copy(forumArticle, ForumArticleVO.class));
    // 有附件
    if (forumArticle.getAttachmentType() == 1) {
      ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
      attachmentQuery.setArticleId(articleId);
      List<ForumArticleAttachment> forumArticleAttachmentList = forumArticleAttachmentService.findListByParam(attachmentQuery);
      if (!forumArticleAttachmentList.isEmpty()) {
        detailVO.setForumArticleAttachmentVO(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
      }
    }
    // 是否已经点赞
    if (sessionWebUserDto != null) {
      LikeRecord likeRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(articleId, sessionWebUserDto.getUserId(), OperRecordOpTypeEnum.ARTICLE_LIKE.getType());
      if (likeRecord != null) {
        detailVO.setHavaLike(true);
      }
    }
    return getSuccessResponseVO(detailVO);
  }

  /**
   * 点赞
   *
   * @param session   用户session
   * @param articleId 文章id
   * @return 点赞结果
   */
  @GetMapping("/doLike")
  @GlobalIntercepter(checkLogin = true, checkParams = true, frequencyType = UserOperFrequencyTypeEnum.DO_LIKE)
  public ResponseVO doLike(HttpSession session, @VerifyParams(required = true) String articleId) {
    SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
    likeRecordService.doLike(articleId, sessionWebUserDto.getUserId(), sessionWebUserDto.getNickName(), OperRecordOpTypeEnum.ARTICLE_LIKE);
    return getSuccessResponseVO(null);
  }

  /**
   * 获取用户积分和下载信息
   *
   * @param session 用户session
   * @param fileId  文件id
   * @return
   */
  @RequestMapping("/getUserDownloadInfo")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO getUserDownloadInfo(HttpSession session, @VerifyParams(required = true) String fileId) {
    SessionWebUserDto webUserDto = getUserInfoFromSession(session);
    UserInfo userInfo = userInfoService.getUserInfoByUserId(webUserDto.getUserId());
    if (userInfo == null) {
      throw new BusinessException(ResponseCodeEnum.CODE_404);
    }
    UserDownloadInfoVO userDownloadInfoVO = new UserDownloadInfoVO();
    userDownloadInfoVO.setUserIntegral(userInfo.getCurrentIntegral());
    ForumArticleAttachmentDownload forumArticleAttachmentDownload = forumArticleAttachmentDownloadService.getForumArticleAttachmentDownloadByFileIdAndUserId(fileId, webUserDto.getUserId());
    if (forumArticleAttachmentDownload != null) {
      userDownloadInfoVO.setHavaDownload(true);
    }
    return getSuccessResponseVO(userDownloadInfoVO);
  }

  /**
   * 下载附件
   *
   * @param session  用户session
   * @param request  请求
   * @param response 响应
   * @param fileId   文件id
   */
  @GetMapping("/attachmentDownload")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public void attachmentDownload(HttpSession session, HttpServletRequest request, HttpServletResponse response, @VerifyParams(required = true) String fileId) {
    ForumArticleAttachment articleAttachment = forumArticleAttachmentService.downloadAttachment(fileId, getUserInfoFromSession(session));
    InputStream in = null;
    OutputStream out = null;
    String downloadFileName = articleAttachment.getFileName();
    String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + articleAttachment.getFilePath();
    File file = new File(filePath);
    try {
      in = new FileInputStream(file);
      out = response.getOutputStream();
      response.setContentType("application/x-msdownload;charset=UTF-8");
      if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0) { // IE浏览器
        downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
      } else {
        downloadFileName = new String(downloadFileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
      }
      response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\"");
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      out.flush();
    } catch (Exception e) {
      logger.error("下载失败", e);
      throw new BusinessException("下载失败");
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        logger.error("下载失败", e);
      }
    }
  }

  /**
   * 发布文章
   *
   * @param session         会话
   * @param title           标题
   * @param pBoardId        父板块
   * @param summary         摘要
   * @param editorType      编辑器类型
   * @param content         内容
   * @param markdownContent markdown内容
   * @param boardId         子板块id
   * @param cover           封面
   * @param attachment      附件
   * @param integral        附件下载所需积分
   * @return
   */

  @RequestMapping("/postArticle")
  @GlobalIntercepter(checkLogin = true, checkParams = true, frequencyType = UserOperFrequencyTypeEnum.POST_ARTICLE)
  public ResponseVO postArticle(HttpSession session,
                                @VerifyParams(required = true, max = 150) String title,
                                @VerifyParams(required = true) Integer pBoardId,
                                @VerifyParams(max = 200) String summary,
                                @VerifyParams(required = true) Integer editorType,
                                @VerifyParams(required = true) String content,
                                String markdownContent,
                                Integer boardId,
                                MultipartFile cover,
                                MultipartFile attachment,
                                Integer integral
  ) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    ForumArticle forumArticle = new ForumArticle();
    forumArticle.setTitle(EscapeUtil.escapeHtml(title));
    forumArticle.setSummary(EscapeUtil.escapeHtml(summary));
    forumArticle.setPBoardId(pBoardId);
    forumArticle.setBoardId(boardId);
    forumArticle.setContent(content);
    EditorTypeEnum typeEnum = EditorTypeEnum.getByType(editorType);
    if (typeEnum == null) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    // 编辑器类型为markdown 但markdown内容为空，抛出异常
    if (EditorTypeEnum.MARKDOWN.getType().equals(editorType) && StringTools.isEmpty(markdownContent)) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    forumArticle.setMarkdownContent(EscapeUtil.escapeHtml(markdownContent));
    forumArticle.setEditorType(editorType);
    forumArticle.setUserId(userDto.getUserId());
    forumArticle.setNickName(userDto.getNickName());
    forumArticle.setUserIpAddress(userDto.getProvince());

    // 附件信息
    ForumArticleAttachment articleAttachment = new ForumArticleAttachment();
    articleAttachment.setIntegral(integral == null ? 0 : integral);
    forumArticleService.postArticle(userDto.getIsAdmin(), forumArticle, articleAttachment, cover, attachment);
    return getSuccessResponseVO(forumArticle.getArticleId());
  }

  /**
   * 更新时获取文章详情
   *
   * @param session   会话
   * @param articleId 文章id
   * @return ResponseVO
   */
  @RequestMapping("articleDetail4Update")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO articleDetail4Update(HttpSession session, @VerifyParams(required = true) String articleId) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(articleId);
    if (forumArticle == null || !forumArticle.getUserId().equals(userDto.getUserId())) {
      throw new BusinessException("文章不存在");
    }
    ForumArticleDetailVO articleDetailVO = new ForumArticleDetailVO();
    articleDetailVO.setForumArticleVO(CopyTools.copy(forumArticle, ForumArticleVO.class));
    if (forumArticle.getAttachmentType() == 1) {
      ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
      attachmentQuery.setArticleId(articleId);
      List<ForumArticleAttachment> forumArticleAttachmentList = forumArticleAttachmentService.findListByParam(attachmentQuery);
      if (!forumArticleAttachmentList.isEmpty()) {
        articleDetailVO.setForumArticleAttachmentVO(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
      }
    }
    return getSuccessResponseVO(articleDetailVO);
  }

  /**
   * 更新文章
   *
   * @param session         会话
   * @param articleId       文章id
   * @param title           标题
   * @param pBoardId        父板块
   * @param summary         摘要
   * @param editorType      编辑器类型
   * @param attachmentType  附件类型
   * @param content         内容
   * @param markdownContent markdown内容
   * @param boardId         子板块id
   * @param cover           封面
   * @param attachment      附件
   * @param integral        附件下载所需积分
   * @return ResponseVO
   */
  @PostMapping("/updateArticle")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO updateArticle(HttpSession session,
                                  @VerifyParams(required = true) String articleId,
                                  @VerifyParams(required = true, max = 150) String title,
                                  @VerifyParams(required = true) Integer pBoardId,
                                  @VerifyParams(max = 200) String summary,
                                  @VerifyParams(required = true) Integer editorType,
                                  @VerifyParams(required = true) Integer attachmentType,
                                  @VerifyParams(required = true) String content,
                                  String markdownContent,
                                  Integer boardId,
                                  MultipartFile cover,
                                  MultipartFile attachment,
                                  Integer integral
  ) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    // 校验文章是否存在
    ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(articleId);
    if (forumArticle == null || !forumArticle.getUserId().equals(userDto.getUserId())) {
      throw new BusinessException("文章不存在");
    }
    // 文章信息
    forumArticle.setMarkdownContent(EscapeUtil.escapeHtml(markdownContent));
    forumArticle.setContent(content);
    forumArticle.setEditorType(editorType);
    forumArticle.setSummary(summary);
    forumArticle.setBoardId(boardId);
    forumArticle.setPBoardId(pBoardId);
    forumArticle.setTitle(title);
    forumArticle.setAttachmentType(attachmentType);
    forumArticle.setUserId(userDto.getUserId());
    forumArticle.setNickName(userDto.getNickName());
    forumArticle.setUserIpAddress(userDto.getProvince());
    // 附件信息
    ForumArticleAttachment articleAttachment = new ForumArticleAttachment();
    articleAttachment.setIntegral(integral == null ? 0 : integral);
    forumArticleService.updateArticle(userDto.getIsAdmin(), forumArticle, articleAttachment, cover, attachment);
    return getSuccessResponseVO(forumArticle.getArticleId());
  }

  /**
   * 搜索文章
   *
   * @param keyword 关键字
   * @return ResponseVO
   */
  @GetMapping("/search")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO search(@VerifyParams(required = true, min = 3) String keyword) {
    ForumArticleQuery query = new ForumArticleQuery();
    query.setTitleFuzzy(keyword);
    PaginationResultVO<ForumArticle> resultVO = forumArticleService.findListByPage(query);
    return getSuccessResponseVO(resultVO);
  }
}
