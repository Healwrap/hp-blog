package com.easybbs.controller.base;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.CopyTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class ABaseController {

  protected static final String STATUC_SUCCESS = "success";

  protected static final String STATUC_ERROR = "error";

  protected <T> ResponseVO getSuccessResponseVO(T t) {
    ResponseVO<T> responseVO = new ResponseVO<>();
    responseVO.setStatus(STATUC_SUCCESS);
    responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
    responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
    responseVO.setData(t);
    return responseVO;
  }

  protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
    ResponseVO vo = new ResponseVO();
    vo.setStatus(STATUC_ERROR);
    if (e.getCode() == null) {
      vo.setCode(ResponseCodeEnum.CODE_600.getCode());
    } else {
      vo.setCode(e.getCode());
    }
    vo.setInfo(e.getMessage());
    vo.setData(t);
    return vo;
  }

  protected <T> ResponseVO getServerErrorResponseVO(T t) {
    ResponseVO vo = new ResponseVO();
    vo.setStatus(STATUC_ERROR);
    vo.setCode(ResponseCodeEnum.CODE_500.getCode());
    vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
    vo.setData(t);
    return vo;
  }

  /**
   * 获取客户端IP地址
   *
   * @param request HttpServletRequest
   * @return ip
   */
  protected String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(',');
      if (index != -1) {
        ip = ip.split(",")[0];
      }
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
    SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSIONS_KEY);
    System.out.println("sessionWebUserDto:" + sessionWebUserDto);
    return sessionWebUserDto;
  }

  protected <S, T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result, Class<T> clazz) {
    PaginationResultVO<T> resultVO = new PaginationResultVO<>();
    resultVO.setList(CopyTools.copyList(result.getList(), clazz));
    resultVO.setPageNo(result.getPageNo());
    resultVO.setPageSize(result.getPageSize());
    resultVO.setPageTotal(result.getPageTotal());
    resultVO.setTotalCount(result.getTotalCount());
    return resultVO;
  }
}
