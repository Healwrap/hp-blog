package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.CreateImageCode;
import com.healwrap.entity.dto.SessionAdminUserDto;
import com.healwrap.entity.dto.SysSettingDto;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.framework.config.AdminConfig;
import com.healwrap.service.EmailCodeService;
import com.healwrap.service.UserInfoService;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.SysCacheUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pepedd
 * @ClassName AccountController
 * @Description 登录、注册等的控制器
 * @Date 2023/4/11 10:09
 */
@RestController
public class AccountController extends ABaseController {
  @Resource
  private EmailCodeService emailCodeService;
  @Resource
  private UserInfoService userInfoService;
  @Resource
  private AdminConfig adminConfig;

  /***
   * 获取验证码
   * @param response 响应
   * @param session 会话
   * @throws IOException IO异常
   */
  @RequestMapping("/checkCode")
  public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
    CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setContentType("image/jpeg");
    String code = vCode.getCode();
    // 登录注册
    session.setAttribute(Constants.CHECK_CODE_KEY, code);
    vCode.write(response.getOutputStream());
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
      System.out.println("code:" + session.getAttribute(Constants.CHECK_CODE_KEY));
      System.out.println("checkCode:" + checkCode);
      if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
        throw new BusinessException("图片验证码错误");
      }
      if (!adminConfig.getAdminAccount().equals(email)) {
        throw new BusinessException("当前账号不是管理员账号");
      }
      if (!StringTools.encodeMd5(adminConfig.getAdminPassword()).equals(password)) {
        throw new BusinessException("账号或密码错误");
      }
      SessionAdminUserDto adminUserDto = new SessionAdminUserDto();
      adminUserDto.setAccount(email);
      session.setAttribute(Constants.SESSIONS_KEY, adminUserDto);
      return getSuccessResponseVO(adminUserDto);
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
   * @param session 会话
   * @return 返回值封装 系统设置
   */
  @RequestMapping("/getSysSetting")
  @GlobalIntercepter()
  public ResponseVO getSysSetting(HttpSession session) {
    // 未登录不获取评论设置
    if (session.getAttribute(Constants.SESSIONS_KEY) == null) {
      return getSuccessResponseVO(null);
    }
    SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();
    Map<String, Object> result = new HashMap<>();
    result.put("commentEnable", sysSettingDto.getCommentSetting().getCommentEnable());
    return getSuccessResponseVO(result);
  }

}
