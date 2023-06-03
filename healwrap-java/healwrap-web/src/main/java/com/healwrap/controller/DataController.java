package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.dto.SessionWebUserDto;
import com.healwrap.entity.enums.UserStatusEnum;
import com.healwrap.entity.enums.article.ArticleStatusEnum;
import com.healwrap.entity.po.UserInfo;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.query.UserIntegralRecordQuery;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.ForumArticleService;
import com.healwrap.service.UserInfoService;
import com.healwrap.service.UserIntegralRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author pepedd
 * @ClassName DataController
 * @Description TODO
 * @Date 2023/6/2 22:45
 */
@RestController
@RequestMapping("/data")
public class DataController extends ABaseController {

  @Resource
  private UserInfoService userInfoService;
  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private UserIntegralRecordService userIntegralRecordService;

  /**
   * 获取用户所有发布文章的数量
   *
   * @param session session
   * @param userId  用户ID
   * @return
   */
  @GetMapping("/getArticlePostCount/{userId}")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO getArticlePostCount(HttpSession session, @VerifyParams(required = true) @PathVariable String userId) {
    // 获取用户所有发布文章
    UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
    if (userInfo == null || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
      return null;
    }
    ForumArticleQuery articleQuery = new ForumArticleQuery();
    articleQuery.setOrderBy("post_time asc");
    articleQuery.setUserId(userId);
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    if (userDto != null) {
      articleQuery.setCurrentUserId(userDto.getUserId());
    } else {
      articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    }
    return getSuccessResponseVO(forumArticleService.findListByParam(articleQuery));
  }

  /**
   * 获取用户积分总数量
   *
   * @param session session
   * @return
   */
  @PostMapping("/getIntegralCount")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO getIntegralCount(HttpSession session) {
    // 获取用户所有积分记录
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    UserIntegralRecordQuery recordQuery = new UserIntegralRecordQuery();
    recordQuery.setUserId(userDto.getUserId());
    return getSuccessResponseVO(userIntegralRecordService.findListByParam(recordQuery));
  }
}
