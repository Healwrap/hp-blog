package com.healwrap.entity.dto;

import lombok.Data;

/**
 * @author pepedd
 * @ClassName SessionWebUserDto
 * @Description 用户信息
 * @Date 2023/4/13 11:41
 */
@Data
public class SessionWebUserDto {
  private String nickName;
  private String province;
  private String userId;
  private Boolean isAdmin;
}
