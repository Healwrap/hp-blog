package com.easybbs.entity.dto;

/**
 * @ClassName SessionWebUserDto
 * @Description 用户信息
 * @Date 2023/4/13 11:41
 * @Created by admin
 */
public class SessionWebUserDto {
  private String nickName;
  private String province;
  private String userId;
  private Boolean isAdmin;

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Boolean getAdmin() {
    return isAdmin;
  }

  public void setAdmin(Boolean admin) {
    isAdmin = admin;
  }
}
