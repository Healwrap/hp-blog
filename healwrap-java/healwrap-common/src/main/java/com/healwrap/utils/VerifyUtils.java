package com.healwrap.utils;

import com.healwrap.entity.enums.VerifyRegexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pepedd
 * @ClassName VerifyUtils
 * @Description 验证工具类
 * @Date 2023/4/13 11:07
 */
public class VerifyUtils {
  public static Boolean verify(String regex, String value) {
    if (StringTools.isEmpty(value))
      return false;
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

  public static Boolean verify(VerifyRegexEnum regex, String value) {
    return verify(regex.getRegex(), value);
  }
}
