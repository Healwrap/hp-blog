package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.query.UserInfoQuery;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName UserController
 * @Description TODO
 * @Date 2023/5/31 18:33
 */
@RestController
@RequestMapping("/user")
public class UserController extends ABaseController {
  @Resource
  private UserInfoService userInfoService;

  /**
   * 获取用户列表
   *
   * @param userInfoQuery 查询条件
   * @return ResponseVO
   */
  @GetMapping("/loadUserList")
  public ResponseVO loadUserList(UserInfoQuery userInfoQuery) {
    userInfoQuery.setOrderBy("join_time desc");
    return getSuccessResponseVO(userInfoService.findListByPage(userInfoQuery));
  }

  /**
   * 更新用户状态
   *
   * @param userId 用户ID
   * @param status 状态
   * @return ResponseVO
   */
  @PatchMapping("/updateUserStatus")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO updateUserStatus(@VerifyParams(required = true) String userId,
                                     @VerifyParams(required = true) Integer status) {
    userInfoService.updateUserStatus(userId, status);
    return getSuccessResponseVO(null);
  }

  /**
   * 发送消息
   *
   * @param userId   用户ID
   * @param message  消息
   * @param integral 积分
   * @return ResponseVO
   */
  @PostMapping("/sendMessage")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO sendMessage(@VerifyParams(required = true) String userId,
                                @VerifyParams(required = true) String message,
                                Integer integral) {
    userInfoService.sendMessage(userId, message, integral);
    return getSuccessResponseVO(null);
  }
}
