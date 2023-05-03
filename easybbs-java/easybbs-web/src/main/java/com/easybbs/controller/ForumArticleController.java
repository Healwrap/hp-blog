package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.GlobalIntercepter;
import com.easybbs.entity.annotation.VerifyParams;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleOrderTypeEnum;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.OperRecordOpTypeEnum;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.ForumArticleAttachmentVO;
import com.easybbs.entity.vo.web.ForumArticleDetailVO;
import com.easybbs.entity.vo.web.ForumArticleVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.CopyTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private ForumArticleAttachmentService forumArticleAttachmentService;
  @Resource
  private LikeRecordService likeRecordService;

  /**
   * 加载文章列表
   * @param session 用户session
   * @param pageNo 页码
   * @param boardId 板块id
   * @param pBoardId 父板块id
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
    forumArticleQuery.setCurrentUserId(sessionWebUserDto == null ? null : sessionWebUserDto.getUserId());
    forumArticleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    ArticleOrderTypeEnum orderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
    orderTypeEnum = orderTypeEnum == null ? ArticleOrderTypeEnum.HOT : orderTypeEnum;
    forumArticleQuery.setOrderBy(orderTypeEnum.getOrderSql());
    PaginationResultVO resultVO = forumArticleService.findListByPage(forumArticleQuery);
    return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
  }

  /**
   * 获取文章详情
   * @param session 用户session
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
        && (sessionWebUserDto == null || !sessionWebUserDto.getUserId().equals(forumArticle.getUserId()) || !sessionWebUserDto.getAdmin()))
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
   * @param session 用户session
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
}
