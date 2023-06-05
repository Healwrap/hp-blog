package com.healwrap.controller.api;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.framework.config.WebConfig;
import com.healwrap.service.SysSettingService;
import com.healwrap.utils.StringTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName InnerApiController
 * @Description TODO
 * @Date 2023/5/19 21:11
 */
@RestController
@RequestMapping("/inner")
public class InnerApiController extends ABaseController {
  @Resource
  private WebConfig webConfig;
  @Resource
  private SysSettingService sysSettingService;

  @GetMapping("refreshSysSetting")
  @GlobalIntercepter(checkParams = true)
  public ResponseVO refreshSysSetting(@VerifyParams(required = true) String appKey,
                                      @VerifyParams(required = true) Long timestamp,
                                      @VerifyParams(required = true) String sign) {
    if (!webConfig.getInnerApiAppKey().equals(appKey)) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    if (System.currentTimeMillis() - timestamp > 1000 * 60 * 5) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    String mySign = StringTools.encodeMd5(appKey + timestamp + webConfig.getInnerApiAppSecret());
    if (!mySign.equals(sign)) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    sysSettingService.refreshCache();
    return getSuccessResponseVO(null);
  }
}
