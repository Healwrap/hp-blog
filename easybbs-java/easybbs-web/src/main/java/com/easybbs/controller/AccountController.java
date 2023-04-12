package com.easybbs.controller;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.CreateImageCode;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * &#064; ClassName AccountController
 * &#064; Description TODO
 * &#064; Date 2023/4/11 10:09
 * &#064; Created by admin
 */
@RestController
public class AccountController extends ABaseController {
  @Resource
  private EmailCodeService emailCodeService;
  @Resource
  private UserInfoService userInfoService;

  /***
   * 获取验证码
   * @param response response
   * @param session session
   * @param type 0:登录注册 1:邮箱
   * @throws IOException IOException
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
   * @param session session
   * @param email 邮箱
   * @param checkCode 验证码
   * @param type 0: 未使用 1: 已使用
   * @return ResponseVO
   * @throws IOException IOException
   */
  @RequestMapping("/sendEmailCode")
  public ResponseVO sendEmailCode(HttpSession session, String email, String checkCode, Integer type) throws IOException {

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


  @RequestMapping("/register")
  public ResponseVO register(HttpSession session, String email, String emailCode, String nickName, String password, String checkCode) throws IOException {
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
//    return getSuccessResponseVO(null);
  }
}
