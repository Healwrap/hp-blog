package com.easybbs.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * &#064; ClassName StringUtils
 * &#064; Description TODO
 * &#064; Date 2023/4/11 13:20
 * &#064; Created by admin
 */
public class StringTools {
  public static Boolean isEmpty(String str) {
    return null == str || "".equals(str.trim()) || "null".equals(str);
  }

  public static String getRandomString(Integer count) {
    return RandomStringUtils.random(count, true, true);
  }
  public static String getRandomNumber(Integer count) {
    return RandomStringUtils.random(count, false, true);
  }
  public static String encodeMd5(String str) {
    return StringTools.isEmpty(str) ? "" : DigestUtils.md5Hex(str);
  }
}
