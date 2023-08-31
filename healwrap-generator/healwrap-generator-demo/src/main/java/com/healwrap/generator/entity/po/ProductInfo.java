package com.healwrap.generator.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * &#064; Description  商品信息
 * &#064; Date 2023/04/11
 */
public class ProductInfo implements Serializable {
  /**
   * 自增ID
   */
  private Integer id;

  /**
   * 公司ID
   */
  private String company_id;

  /**
   * 商品编号
   */
  private String code;

  /**
   * 商品名称
   */
  private String product_name;

  /**
   * 价格
   */
  private BigDecimal price;

  /**
   * sku类型
   */
  private Integer sku_type;

  /**
   * 颜色类型
   */
  private Integer color_type;

  /**
   * 创建时间
   */
  private Date create_time;

  /**
   * 创建日期
   */
  private Date create_date;

  /**
   * 库存
   */
  private Long stock;

  /**
   * 状态
   */
  private Integer status;

}
