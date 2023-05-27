package com.healwrap.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pepedd
 * @ClassName UserInfoVO
 * @Description TODO
 * @Date 2023/5/18 11:49
 */
@Data
public class UserInfoVO implements Serializable {

  /**
   * 用户ID
   */
  private String userId;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 邮箱
   */
  private String email;

  /**
   * 0:女 1:男
   */
  private Integer sex;

  /**
   * 个人描述
   */
  private String personDescription;

  /**
   * 加入时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date joinTime;

  /**
   * 最后登录时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date lastLoginTime;


  /**
   * 最后登录ip地址
   */
  private String lastLoginIpAddress;

  /**
   * 当前积分
   */
  private Integer currentIntegral;

  private Integer postCount;
  private Integer likeCount;

}
