package com.healwrap.controller;

import com.healwrap.config.AdminConfig;
import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.ForumArticleAttachment;
import com.healwrap.entity.query.ForumArticleAttachmentQuery;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.query.ForumCommentQuery;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.service.ForumArticleAttachmentService;
import com.healwrap.service.ForumArticleService;
import com.healwrap.service.ForumCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
 * @Description TODO
 * @Date 2023/5/25 7:06
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
  private ForumCommentService forumCommentService;
  @Resource
  private AdminConfig adminConfig;

  /**
   * 加载文章列表
   *
   * @param articleQuery 查询条件
   * @return ResponseVO
   */
  @PostMapping("/loadArticleList")
  public ResponseVO loadArticleList(ForumArticleQuery articleQuery) {
    articleQuery.setOrderBy("post_time desc");
    return getSuccessResponseVO(forumArticleService.findListByPage(articleQuery));
  }

  /**
   * 删除文章
   *
   * @param articleIds 文章id
   * @return ResponseVO
   */
  @DeleteMapping("/deleteArticle")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO deleteArticle(@VerifyParams(required = true) String articleIds) {
    forumArticleService.delArticle(articleIds);
    return getSuccessResponseVO(null);
  }

  /**
   * 更新文章板块信息
   *
   * @param articleId 文章id
   * @param pBoardId  父板块id
   * @param boardId   板块id
   * @return ResponseVO
   */
  @PutMapping("/updateBoard")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO updateBoard(@VerifyParams(required = true) String articleId,
                                @VerifyParams(required = true) Integer pBoardId,
                                @VerifyParams(required = true) Integer boardId) {

    boardId = boardId == null ? 0 : boardId;
    ForumArticle article = new ForumArticle();
    article.setPBoardId(pBoardId);
    article.setBoardId(boardId);
    forumArticleService.updateBoard(articleId, pBoardId, boardId);
    return getSuccessResponseVO(null);
  }

  /**
   * 获取附件信息
   *
   * @param articleId 文章id
   * @return ResponseVO
   */
  @PostMapping("/getAttachment")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO getAttachment(@VerifyParams(required = true) String articleId) {
    ForumArticleAttachmentQuery articleAttachmentQuery = new ForumArticleAttachmentQuery();
    articleAttachmentQuery.setArticleId(articleId);
    List<ForumArticleAttachment> attachmentList = forumArticleAttachmentService.findListByParam(articleAttachmentQuery);
    if (attachmentList.isEmpty()) {
      throw new BusinessException("附件不存在");
    }
    return getSuccessResponseVO(attachmentList.get(0));
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
    ForumArticleAttachment articleAttachment = forumArticleAttachmentService.getForumArticleAttachmentByFileId(fileId);
    InputStream in = null;
    OutputStream out = null;
    String downloadFileName = articleAttachment.getFileName();
    String filePath = adminConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + articleAttachment.getFilePath();
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
   * 置顶文章
   *
   * @param articleId 文章id
   * @param topType   置顶类型
   * @return ResponseVO
   */
  @PutMapping("/topArticle")
  public ResponseVO topArticle(@VerifyParams(required = true) String articleId, Integer topType) {
    ForumArticle article = new ForumArticle();
    article.setTopType(topType);
    forumArticleService.updateForumArticleByArticleId(article, articleId);
    return getSuccessResponseVO(null);
  }

  /**
   * 审核文章
   *
   * @param articleIds 文章id
   * @return ResponseVO
   */
  @DeleteMapping("/auditArticle")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO auditArticle(@VerifyParams(required = true) String articleIds) {
    forumArticleService.auditArticle(articleIds);
    return getSuccessResponseVO(null);
  }

  /**
   * 加载评论列表
   *
   * @param commentQuery 查询条件
   * @return ResponseVO
   */
  @PostMapping("/loadCommentList")
  public ResponseVO loadCommentList(ForumCommentQuery commentQuery) {
    commentQuery.setLoadChildren(true);
    commentQuery.setOrderBy("post_time desc");
    return getSuccessResponseVO(forumCommentService.findListByParam(commentQuery));
  }

  /**
   * 加载文章评论列表
   *
   * @param commentQuery 查询条件
   * @return ResponseVO
   */
  @PostMapping("/loadComment4Article")
  public ResponseVO loadComment4Article(ForumCommentQuery commentQuery) {
    commentQuery.setLoadChildren(true);
    commentQuery.setOrderBy("post_time desc");
    commentQuery.setPCommentId(0);
    return getSuccessResponseVO(forumCommentService.findListByParam(commentQuery));
  }

  /**
   * 删除评论
   *
   * @param commentIds 评论id数组
   * @return ResponseVO
   */
  @DeleteMapping("/deleteComment")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO deleteComment(@VerifyParams(required = true) String commentIds) {
    forumCommentService.delComment(commentIds);
    return getSuccessResponseVO(null);
  }
}
