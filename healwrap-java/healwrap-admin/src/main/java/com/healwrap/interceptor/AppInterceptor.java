package com.healwrap.interceptor;

import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.SessionAdminUserDto;
import com.healwrap.framework.config.AdminConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author pepedd
 * @ClassName AppInterceptor
 * @Description TODO
 * @Date 2023/5/25 6:39
 */
@Component("appInterceptor")
public class AppInterceptor implements HandlerInterceptor {
  @Resource
  private AdminConfig adminConfig;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (null == handler) {
      return false;
    }
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    /**
     * 全局拦截器
     */
    if (request.getRequestURI().contains("checkCode") || request.getRequestURI().contains("login")) {
      return true;
    }
    checkLogin();
    return true;
  }

  private void checkLogin() {
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
    HttpSession session = request.getSession();
    SessionAdminUserDto userDto = (SessionAdminUserDto) session.getAttribute(Constants.SESSIONS_KEY);
    if (userDto == null && adminConfig.getDev()) {
      userDto = new SessionAdminUserDto();
      userDto.setAccount("管理员");
      session.setAttribute(Constants.SESSIONS_KEY, userDto);
    }
    if (userDto == null) {
      throw new RuntimeException("请登录");
    }
  }
}
