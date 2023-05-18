package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.VerifyParams;
import com.easybbs.entity.enums.UserStatusEnum;
import com.easybbs.entity.enums.article.ArticleStatusEnum;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.LikeRecordQuery;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.UserInfoVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.LikeRecordService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.CopyTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName UserController
 * @Description 用户控制器
 * @Date 2023/5/18 10:48
 */
@RestController
@RequestMapping("/user")
public class UserController extends ABaseController {
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private LikeRecordService likeRecordService;

  @RequestMapping("/getUserInfo")
  public ResponseVO getUserInfo(@VerifyParams(required = true) String userId) {
    UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
    if (userInfo == null || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
      throw new BusinessException("用户不存在");
    }
    ForumArticleQuery articleQuery = new ForumArticleQuery();
    articleQuery.setUserId(userId);
    articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    Integer postCount = forumArticleService.findCountByParam(articleQuery);

    UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
    userInfoVO.setPostCount(postCount);

    LikeRecordQuery likeRecordQuery = new LikeRecordQuery();
    likeRecordQuery.setAuthorUserId(userId);
    Integer likeCount = likeRecordService.findCountByParam(likeRecordQuery);

    userInfoVO.setLikeCount(likeCount);
    return getSuccessResponseVO(userInfoVO);
  }

}
