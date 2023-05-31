package com.healwrap.generator.bean;

/**
 * &#064;ClassName  FieldInfo
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 17:57
 * &#064;Created  by admin
 */
public class FieldInfo {
  /***
   * 字段名称
   */
  private String FieldName;
  /***
   * bean属性名称
   */
  private String propertyName;
  private String sqlType;
  /***
   * 字段类型
   */
  private String javaType;
  /***
   * 字段备注
   */
  private String commnet;
  /***
   * 字段是否自增长
   */
  private Boolean isAutoIncrement;

  public String getFieldName() {
    return FieldName;
  }

  public void setFieldName(String fieldName) {
    FieldName = fieldName;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getSqlType() {
    return sqlType;
  }

  public void setSqlType(String sqlType) {
    this.sqlType = sqlType;
  }

  public String getJavaType() {
    return javaType;
  }

  public void setJavaType(String javaType) {
    this.javaType = javaType;
  }

  public String getCommnet() {
    return commnet;
  }

  public void setCommnet(String commnet) {
    this.commnet = commnet;
  }

  public Boolean getAutoIncrement() {
    return isAutoIncrement;
  }

  public void setAutoIncrement(Boolean autoIncrement) {
    isAutoIncrement = autoIncrement;
  }
}
