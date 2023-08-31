package com.healwrap.entity.vo.web;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pepedd
 * @ClassName UserDownloadInfoVO
 * @Description TODO
 * @Date 2023/5/4 20:14
 */
@Data
public class UserDownloadInfoVO implements Serializable {
  private Integer userIntegral;
  private Boolean havaDownload;
}
