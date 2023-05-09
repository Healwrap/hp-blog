package com.easybbs.entity.enums;

/**
 * @ClassName VerifyRegexEnum
 * @Description 校验正则
 * @Date 2023/4/13 10:28
 * @Created by admin
 */
public enum VerifyRegexEnum {
  NO("", "不校验"),
  IP("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}", "IP地址"),
  POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$", "正整数"),
  NUMBER_LETTER_UNDERLINE("^[a-zA-Z0-9_]+$", "数字字母下划线"),
  EMAIL("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", "邮箱"),
  PHONE("^[1][3,4,5,7,8][0-9]{9}$", "手机号"),
  COMMON("^[\\\\u4e00-\\\\u9fa5_a-zA-Z0-9]+$","数字、字母、下划线、中文"),
  PASSWORD("^[0-9A-Za-z]{8,18}$","数字、字母、8-18位"),
  ACCOUNT("^[a-zA-Z][a-zA-Z0-9_]{1,11}$","字母开头，由数字、字母、下划线组成，2-12位"),
  MONEY("^[0-9]+(.[0-9]{1,2})?$","金额");

  private final String regex;
  private final String desc;

  VerifyRegexEnum(String regex, String desc) {
    this.regex = regex;
    this.desc = desc;
  }

  public String getRegex() {
    return regex;
  }

  public String getDesc() {
    return desc;
  }
}
