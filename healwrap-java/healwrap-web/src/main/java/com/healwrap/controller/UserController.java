package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.dto.SessionWebUserDto;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.entity.enums.UserStatusEnum;
import com.healwrap.entity.enums.article.ArticleStatusEnum;
import com.healwrap.entity.enums.message.MessageTypeEnum;
import com.healwrap.entity.po.ForumArticle;
import com.healwrap.entity.po.UserInfo;
import com.healwrap.entity.po.UserIntegralRecord;
import com.healwrap.entity.po.UserMessage;
import com.healwrap.entity.query.ForumArticleQuery;
import com.healwrap.entity.query.LikeRecordQuery;
import com.healwrap.entity.query.UserIntegralRecordQuery;
import com.healwrap.entity.query.UserMessageQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.entity.vo.UserInfoVO;
import com.healwrap.entity.vo.web.ForumArticleVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.service.*;
import com.healwrap.utils.CopyTools;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
  @Resource
  private UserIntegralRecordService userIntegralRecordService;
  @Resource
  private UserMessageService userMessageService;

  /**
   * 获取用户信息
   *
   * @param userId 用户id
   * @return ResponseVO
   */
  @PostMapping("/getUserInfo")
  @GlobalIntercepter(checkParams = true)
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

  /**
   * 修改用户信息
   *
   * @param session     会话
   * @param sex         性别
   * @param description 个人描述
   * @param avatar      头像
   * @return ResponseVO
   */
  @PutMapping("updateUserInfo")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO updateUserInfo(HttpSession session, Integer sex, @VerifyParams(max = 100) String description, MultipartFile avatar) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(userDto.getUserId());
    userInfo.setSex(sex);
    userInfo.setPersonDescription(description);
    userInfoService.updateUserInfo(userInfo, avatar);
    return getSuccessResponseVO(null);
  }

  /**
   * 获取用户文章列表
   *
   * @param session 会话
   * @param userId  用户id
   * @param type    类型 0:发表 1:评论 2:点赞
   * @param pageNo  页码
   * @return ResponseVO
   */
  @PostMapping("getUserArticleList")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO getUserArticleList(HttpSession session, @VerifyParams(required = true) String userId, @VerifyParams(required = true) Integer type, Integer pageNo) {
    UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
    if (userInfo == null || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
      throw new BusinessException("用户不存在");
    }
    ForumArticleQuery articleQuery = new ForumArticleQuery();
    articleQuery.setOrderBy("post_time desc");
    articleQuery.setPageNo(pageNo);
    if (type == 0) {
      articleQuery.setUserId(userId);
    } else if (type == 1) {
      articleQuery.setCommentUserId(userId);
    } else if (type == 2) {
      articleQuery.setLikeUserId(userId);
    }

    SessionWebUserDto userDto = getUserInfoFromSession(session);
    if (userDto != null) {
      articleQuery.setCurrentUserId(userDto.getUserId());
    } else {
      articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
    }
    PaginationResultVO<ForumArticle> resultVO = forumArticleService.findListByPage(articleQuery);
    return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
  }

  /**
   * 获取用户积分记录
   *
   * @param session         会话
   * @param pageNo          页码
   * @param createTimeStart 开始时间
   * @param createTimeEnd   结束时间
   * @return ResponseVO
   */
  @PostMapping("/getUserIntegralRecord")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO getUserIntegralRecord(HttpSession session, Integer pageNo, String createTimeStart, String createTimeEnd) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    UserIntegralRecordQuery recordQuery = new UserIntegralRecordQuery();
    recordQuery.setUserId(userDto.getUserId());
    recordQuery.setPageNo(pageNo);
    recordQuery.setCreateTimeStart(createTimeStart);
    recordQuery.setCreateTimeEnd(createTimeEnd);
    recordQuery.setOrderBy("record_id desc");
    PaginationResultVO<UserIntegralRecord> resultVO = userIntegralRecordService.findListByPage(recordQuery);
    return getSuccessResponseVO(resultVO);
  }

  /**
   * 获取用户消息数量
   *
   * @param session 会话
   * @return ResponseVO
   */
  @GetMapping("getMessageCount")
  @GlobalIntercepter(checkLogin = true)
  public ResponseVO getMessageCount(HttpSession session) {
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    return getSuccessResponseVO(userMessageService.getUserMessageCountDto(userDto.getUserId()));
  }

  /**
   * 获取用户消息列表
   *
   * @param session 会话
   * @param code    消息类型 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
   * @param pageNo  页码
   * @return ResponseVO
   */
  @PostMapping("getMessageList")
  @GlobalIntercepter(checkLogin = true, checkParams = true)
  public ResponseVO getMessageList(HttpSession session, @VerifyParams(required = true) String code, Integer pageNo) {
    MessageTypeEnum typeEnum = MessageTypeEnum.getByCode(code);
    if (typeEnum == null) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    SessionWebUserDto userDto = getUserInfoFromSession(session);
    UserMessageQuery query = new UserMessageQuery();
    query.setPageNo(pageNo);
    query.setReceivedUserId(userDto.getUserId());
    query.setMessageType(typeEnum.getType());
    query.setOrderBy("message_id desc");
    PaginationResultVO<UserMessage> resultVO = userMessageService.findListByPage(query);
    if (pageNo == null || pageNo == 1) {
      userMessageService.readMessageByType(userDto.getUserId(), typeEnum.getType());
    }
    return getSuccessResponseVO(resultVO);
  }


}
