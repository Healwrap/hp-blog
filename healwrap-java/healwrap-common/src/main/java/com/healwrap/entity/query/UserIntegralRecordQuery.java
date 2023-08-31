package com.healwrap.entity.query;

import lombok.Data;


/**
 * 用户积分记录表参数
 */
@Data
public class UserIntegralRecordQuery extends BaseParam {


  /**
   * 记录ID
   */
  private Integer recordId;

  /**
   * 用户ID
   */
  private String userId;

  private String userIdFuzzy;

  /**
   * 操作类型
   */
  private Integer operType;

  /**
   * 积分
   */
  private Integer integral;

  /**
   * 创建时间
   */
  private String createTime;

  private String createTimeStart;

  private String createTimeEnd;

}
