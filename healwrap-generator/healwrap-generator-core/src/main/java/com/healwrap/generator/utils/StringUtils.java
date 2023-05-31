package com.healwrap.generator.utils;

/**
 * &#064;ClassName  StringUtils
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 18:43
 * &#064;Created  by admin
 */
public class StringUtils {
  public static String upperCaseFirstLetter(String field) {
    if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
      return "";
    }
    return field.substring(0, 1).toUpperCase() + field.substring(1);
  }

  public static String lowerCaseFirstLetter(String field) {
    if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
      return "";
    }
    return field.substring(0, 1).toLowerCase() + field.substring(1);
  }

  public static void main(String[] args) {
    System.out.println(lowerCaseFirstLetter("Name"));
  }
}
