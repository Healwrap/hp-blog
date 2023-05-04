package com.easybbs.entity.vo.web;

/**
 * @ClassName UserDownloadInfoVO
 * @Description TODO
 * @Date 2023/5/4 20:14
 * @Created by pepedd
 */
public class UserDownloadInfoVO {
  private Integer userIntegral;
  private Boolean havaDownload;

  public Integer getUserIntegral() {
    return userIntegral;
  }

  public void setUserIntegral(Integer userIntegral) {
    this.userIntegral = userIntegral;
  }

  public Boolean getHavaDownload() {
    return havaDownload;
  }

  public void setHavaDownload(Boolean havaDownload) {
    this.havaDownload = havaDownload;
  }
}
