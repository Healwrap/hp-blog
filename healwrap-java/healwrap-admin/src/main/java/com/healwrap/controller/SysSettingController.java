package com.healwrap.controller;

import com.healwrap.config.AdminConfig;
import com.healwrap.config.WebConfig;
import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.dto.SysSettingDto;
import com.healwrap.entity.dto.sysDto.*;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.SysSettingService;
import com.healwrap.utils.JsonUtils;
import com.healwrap.utils.OkHttpUtils;
import com.healwrap.utils.StringTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName SysSettingController
 * @Description TODO
 * @Date 2023/5/31 17:56
 */
@RestController
@RequestMapping("/setting")
public class SysSettingController extends ABaseController {
  @Resource
  private SysSettingService sysSettingService;

  @Resource
  private AdminConfig adminConfig;
  @Resource
  private WebConfig webConfig;

  /**
   * 获取设置
   *
   * @return ResponseVO
   */
  @GetMapping("/getSetting")
  public ResponseVO getSetting() {
    return getSuccessResponseVO(sysSettingService.refreshCache());
  }

  /**
   * 保存设置
   *
   * @param sysSetting4AuditDto    审核设置
   * @param sysSetting4CommentDto  评论设置
   * @param sysSetting4EmailDto    邮件设置
   * @param sysSetting4LikeDto     点赞设置
   * @param sysSetting4PostDto     文章设置
   * @param sysSetting4RegisterDto 注册设置
   * @return ResponseVO
   */
  @PatchMapping("/saveSetting")
  public ResponseVO saveSetting(SysSetting4AuditDto sysSetting4AuditDto,
                                SysSetting4CommentDto sysSetting4CommentDto,
                                SysSetting4EmailDto sysSetting4EmailDto,
                                SysSetting4LikeDto sysSetting4LikeDto,
                                SysSetting4PostDto sysSetting4PostDto,
                                SysSetting4RegisterDto sysSetting4RegisterDto) {
    SysSettingDto sysSettingDto = new SysSettingDto();
    sysSettingDto.setAuditSetting(sysSetting4AuditDto);
    sysSettingDto.setCommentSetting(sysSetting4CommentDto);
    sysSettingDto.setEmailSetting(sysSetting4EmailDto);
    sysSettingDto.setLikeSetting(sysSetting4LikeDto);
    sysSettingDto.setPostSetting(sysSetting4PostDto);
    sysSettingDto.setRegisterSetting(sysSetting4RegisterDto);
    sysSettingService.saveSetting(sysSettingDto);
    sendWebRequest();
    return getSuccessResponseVO(null);
  }

  /**
   * 发送刷新访客端缓存请求
   */
  private void sendWebRequest() {
    String appKey = webConfig.getInnerApiAppKey();
    String appSecret = webConfig.getInnerApiAppSecret();
    Long timestamp = System.currentTimeMillis();
    String sign = StringTools.encodeMd5(appKey + appSecret + timestamp);
    String url = adminConfig.getWebApiUrl() + "?appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
    String responseJson = OkHttpUtils.getRequest(url);
    ResponseVO responseVO = JsonUtils.convertJson2Object(responseJson, ResponseVO.class);
    if (!STATUC_SUCCESS.equals(responseVO.getStatus())) {
      throw new RuntimeException("刷新访客端缓存失败");
    }
  }
}
