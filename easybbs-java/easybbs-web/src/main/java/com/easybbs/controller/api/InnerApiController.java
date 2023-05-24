package com.easybbs.controller.api;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.GlobalIntercepter;
import com.easybbs.entity.annotation.VerifyParams;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.SysSettingService;
import com.easybbs.utils.StringTools;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("refreshSysSetting")
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
