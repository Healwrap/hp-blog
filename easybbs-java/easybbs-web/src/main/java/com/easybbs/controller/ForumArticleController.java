package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.GlobalIntercepter;
import com.easybbs.entity.annotation.VerifyParams;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleOrderTypeEnum;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.OperRecordOpTypeEnum;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.*;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.ForumArticleAttachmentVO;
import com.easybbs.entity.vo.web.ForumArticleDetailVO;
import com.easybbs.entity.vo.web.ForumArticleVO;
import com.easybbs.entity.vo.web.UserDownloadInfoVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.*;
import com.easybbs.utils.CopyTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ForumArticleController
 * @Description 论坛文章控制器
 * @Date 2023/4/24 22:39
 * @Created by admin
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
    forumArticleQuery.setpBoardId(pBoardId);
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
    if (Objects.equals(forumArticle.getAttachmentType(), Constants.ONE)) {
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
  @GlobalIntercepter(checkLogin = true, checkParams = true)
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
}
