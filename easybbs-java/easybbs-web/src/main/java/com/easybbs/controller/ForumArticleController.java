package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleOrderTypeEnum;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.ForumArticleVO;
import com.easybbs.service.ForumArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

  @GetMapping("/loadArticle")
  public ResponseVO loadArticle(HttpSession session, Integer boardId, Integer pBoardId, Integer orderType, Integer pageNo) {
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
}
