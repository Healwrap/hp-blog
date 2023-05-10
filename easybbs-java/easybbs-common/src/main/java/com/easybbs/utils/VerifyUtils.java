package com.easybbs.utils;

import com.easybbs.entity.enums.VerifyRegexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName VerifyUtils
 * @Description 验证工具类
 * @Date 2023/4/13 11:07
 * @author pepedd
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
