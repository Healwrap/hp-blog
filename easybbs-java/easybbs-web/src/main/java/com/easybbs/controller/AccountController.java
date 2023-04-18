package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.GlobalIntercepter;
import com.easybbs.entity.annotation.VerifyParams;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.CreateImageCode;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.dto.SysSettingDto;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.VerifyRegexEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.StringTools;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AccountController
 * @Description 登录、注册等的控制器
 * @Date 2023/4/11 10:09
 * @Created by admin
 */
@RestController
public class AccountController extends ABaseController {
  @Resource
  private EmailCodeService emailCodeService;
  @Resource
  private UserInfoService userInfoService;

  /***
   * 获取验证码
   * @param response 响应
   * @param session 会话
   * @param type 0:登录注册 1:邮箱
   * @throws IOException IO异常
   */
  @RequestMapping("/checkCode")
  public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
    CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setContentType("image/jpeg");
    String code = vCode.getCode();
    // 登录注册
    if (type == null || type == 0) {
      session.setAttribute(Constants.CHECK_CODE_KEY, code);
    } else {
      // 获取邮箱
      session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
    }
    vCode.write(response.getOutputStream());
  }

  /***
   * 发送邮箱验证码
   * @param session 会话
   * @param email 邮箱
   * @param checkCode 验证码
   * @param type 0: 未使用 1: 已使用
   * @return 返回值封装
   * @throws IOException IO异常
   */
  @RequestMapping("/sendEmailCode")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO sendEmailCode(HttpSession session,
                                  @VerifyParams(required = true, regex = VerifyRegexEnum.EMAIL) String email,
                                  @VerifyParams(required = true) String checkCode,
                                  @VerifyParams(required = true) Integer type) {

    try {
      if (StringTools.isEmpty(email) || StringTools.isEmpty(checkCode) || type == null) {
        throw new BusinessException(ResponseCodeEnum.CODE_600);
      }
      if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
        throw new BusinessException("图片验证码错误");
      }
      emailCodeService.sendEmailCode(email, type);
      return getSuccessResponseVO(null);
    } finally {
      session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
    }
  }

  /**
   * 注册
   *
   * @param session   会话
   * @param email     邮箱
   * @param emailCode 邮箱验证码
   * @param nickName  昵称
   * @param password  密码
   * @param checkCode 验证码
   * @return 返回值封装
   */

  @RequestMapping("/register")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO register(HttpSession session,
                             @VerifyParams(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                             @VerifyParams(required = true) String emailCode,
                             @VerifyParams(required = true, max = 20) String nickName,
                             @VerifyParams(required = true, regex = VerifyRegexEnum.PASSWORD) String password,
                             @VerifyParams(required = true) String checkCode) {
    try {

      if (StringTools.isEmpty(email) || StringTools.isEmpty(emailCode) || StringTools.isEmpty(nickName) || StringTools.isEmpty(password) || StringTools.isEmpty(checkCode)) {
        throw new BusinessException(ResponseCodeEnum.CODE_600);
      }
      if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
        throw new BusinessException("图片验证码错误");
      }
      userInfoService.register(email, emailCode, nickName, password);
      return getSuccessResponseVO(null);
    } finally {
      session.removeAttribute(Constants.CHECK_CODE_KEY);
    }
  }

  /**
   * 登录
   *
   * @param session   会话
   * @param email     邮箱
   * @param password  密码
   * @param checkCode 验证码
   * @return 返回值封装 用户信息
   */

  @RequestMapping("/login")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO login(HttpSession session,
                          @VerifyParams(required = true) String email,
                          @VerifyParams(required = true) String password,
                          @VerifyParams(required = true) String checkCode) {
    try {
      if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
        throw new BusinessException("图片验证码错误");
      }
      SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password, "");
      session.setAttribute(Constants.SESSIONS_KEY, sessionWebUserDto);
      return getSuccessResponseVO(getUserInfoFromSession(session));
    } finally {
      session.removeAttribute(Constants.CHECK_CODE_KEY);
    }
  }

  /**
   * 获取用户信息
   *
   * @param session 会话
   * @return 返回值封装 用户信息
   */

  @RequestMapping("/getUserInfo")
  @GlobalIntercepter()
  public ResponseVO getUserInfo(HttpSession session) {
    return getSuccessResponseVO(getUserInfoFromSession(session));
  }

  /**
   * 退出登录
   *
   * @param session 会话
   * @return 返回值封装 null
   */

  @RequestMapping("/logout")
  @GlobalIntercepter()
  public ResponseVO logout(HttpSession session) {
    session.invalidate();
    return getSuccessResponseVO(null);
  }

  /**
   * 获取系统设置
   *
   * @return 返回值封装 系统设置
   */
  @RequestMapping("/getSysSetting")
  @GlobalIntercepter()
  public ResponseVO getSysSetting() {
    SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();
    Map<String, Object> result = new HashMap<>();
    result.put("commentEnable", sysSettingDto.getCommentSetting().getCommentEnable());
    return getSuccessResponseVO(result);
  }

  @RequestMapping("/resetPwd")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO resetPwd(HttpSession session,
                             @VerifyParams(required = true, regex = VerifyRegexEnum.EMAIL) String email,
                             @VerifyParams(required = true, min = 8, max = 18, regex = VerifyRegexEnum.PASSWORD) String password,
                             @VerifyParams(required = true) String checkCode,
                             @VerifyParams(required = true) String emailCode) {
    try {
      if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
        throw new BusinessException("图片验证码错误");
      }
      userInfoService.resetPwd(email, password, emailCode);
      return getSuccessResponseVO(null);
    } finally {
      session.removeAttribute(Constants.CHECK_CODE_KEY);
    }

  }


}
